package xyz.derkades.metadatasaver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import xyz.derkades.derkutils.FileUtils;
import xyz.derkades.metadatasaver.file.audio.FlacMetadata;
import xyz.derkades.metadatasaver.file.audio.Mp3MetadataJAudioTagger;
import xyz.derkades.metadatasaver.file.audio.MusicMetadata;
import xyz.derkades.plex4j.Server;
import xyz.derkades.plex4j.library.Library;
import xyz.derkades.plex4j.library.MusicLibrary;
import xyz.derkades.plex4j.library.item.Album;
import xyz.derkades.plex4j.library.item.Artist;
import xyz.derkades.plex4j.library.item.Track;

public class Main {
	
	/*
	 * TODO Multiple libraries support
	 * TODO Album image support
	 * TODO Video support
	 */
	
	/**
	 * This program will write both ID3v1 and ID3v2 tags.
	 * It will only read ID3v2 data though. To read ID3v1 tags instead of ID3v2 tags, set this option to false.
	 */
	public static final boolean USE_ID3V2 = true;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		File config = new File("config.yml");
		
		if (!config.exists()) {			
			saveDefaultConfig(config);
			System.out.println("A config file has been generated at " + config.getAbsolutePath());
			System.out.println("Enter your plex server details in the configuration file and run the program again.");
			System.exit(0);
		}
		
		Yaml yaml = new Yaml();
		
		Map<String, Object> map;
		
		try {
			map = (Map<String, Object>) yaml.load(new FileInputStream(config));
		} catch (FileNotFoundException e) {
			throw new AssertionError(e);
		}
		
		URL url;
		String token;
		int libraryId;
		boolean continueWithErrors, acceptWarning;
		
		try {
			url = new URL((String) map.get("url"));
			token = (String) map.get("token");
			libraryId = (int) map.get("library");
			continueWithErrors = (boolean) map.get("continue-with-errors");
			acceptWarning = (boolean) map.get("accept-warning");
		} catch (MalformedURLException e) {
			System.err.println("Your URL is malformed.");
			System.exit(1);
			// https://stackoverflow.com/questions/12546477/eclipse-doesnt-think-system-exit-interrupts-execution
			throw new AssertionError();  
		} catch (ClassCastException e) {
			System.err.println("One or more config options are of an invalid type.");
			System.err.println("url                   -  String");
			System.err.println("token                 -  String");
			System.err.println("library               -  Integer");
			System.err.println("continue-with-errors  -  Boolean");
			System.err.println("accept-warning        -  Boolean");
			System.exit(1);
			// https://stackoverflow.com/questions/12546477/eclipse-doesnt-think-system-exit-interrupts-execution
			throw new AssertionError();
		}
		
		if (!acceptWarning) {
			System.out.println("Please read and accept the warning at the bottom of the configuration file and run the program again.");
			System.exit(0);
		}
		
		long startTime = System.currentTimeMillis();
		
		List<Track> tracks = new ArrayList<>();
		
		try {			
			Server server = new Server(url, token);
			
			List<Library> libraries = server.getLibraries();
			
			if (libraryId < 0) {
				// List libraries and exit
				for (Library library : libraries) {
					if (!(library instanceof MusicLibrary)) {
						continue;
					}
					
					System.out.printf("%s: %s", library.getId(), library.getTitle());
					System.out.println();
				}
				System.exit(0);
			}
			
			Library selectedGenericLibrary = null;
			
			for (Library library : libraries) {
				if (library.getId() == libraryId) {
					selectedGenericLibrary = library;
				}
			}
			
			if (selectedGenericLibrary == null) {
				System.err.println("A library with this id could not be found. Did you enter an id that was in the list above?");
				System.exit(0);
			}
			
			if (!(selectedGenericLibrary instanceof MusicLibrary)) {
				System.err.println("This library is not a music library. Set library to -1 to print a list of music libraries.");
				System.exit(0);
			}
			
			MusicLibrary selectedLibrary = (MusicLibrary) selectedGenericLibrary;
			
			for (Artist artist : selectedLibrary.getArtists()) {
				for (Album album : artist.getAlbums()) {
					tracks.addAll(album.getTracks());
				}
			}
		} catch (IOException | SAXException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Done getting track list from plex!");
		System.out.printf("Found %s tracks in %s seconds.\n", tracks.size(), (System.currentTimeMillis() - startTime) / 1000);
		
		long metadataSavingStartTime = System.currentTimeMillis();
		int modifiedTracks = 0;
		
		for (Track track : tracks) {
			try {
				File file = new File(track.getFile());
				
				if (file.exists()) {
					System.err.println("The file " + file.getAbsolutePath() + " could not be found. Are you running this program on the plex media server server?");
					continue;
				}
				
				MusicMetadata metadata;
				
				if (track.getFile().endsWith(".mp3")) {
					metadata = new Mp3MetadataJAudioTagger(file);
				} else if (track.getFile().endsWith(".flac")) {
					metadata = new FlacMetadata(file);
				} else {
					System.err.println("Unsupported file type " + track.getFile());
					continue;
				}
				
				// TODO Add support for all metadata types in MusicMetadata class
				
				String plexAlbum = track.getAlbum().getTitle();
				String plexAlbumArtist = track.getAlbum().getArtist().getTitle();
				String plexArtist = "Artist";
				String plexTitle = track.getTitle();
				String plexTrack = "1";
				String plexYear = "2023";
				
				List<String> logMessages = new ArrayList<>();
				
				if (!metadata.getAlbum().equals(plexAlbum)) {
					logMessages.add(String.format("Album: %s -> %s", metadata.getAlbum(), plexAlbum));
					metadata.setAlbum(plexAlbum);
				}
				
				if (!metadata.getAlbumArtist().equals(plexAlbumArtist)) {
					logMessages.add(String.format("Album artist: %s -> %s", metadata.getAlbumArtist(), plexAlbumArtist));
					metadata.setAlbum(plexAlbumArtist);
				}
				
				if (!metadata.getArtist().equals(plexArtist)) {
					logMessages.add(String.format("Artist: %s -> %s", metadata.getArtist(), plexArtist));
					metadata.setAlbum(plexArtist);
				}
				
				if (!metadata.getTitle().equals(plexTitle)) {
					logMessages.add(String.format("Title: %s -> %s", metadata.getTitle(), plexTitle));
					metadata.setAlbum(plexTitle);
				}
				
				if (!metadata.getTrack().equals(plexTrack)) {
					logMessages.add(String.format("Track: %s -> %s", metadata.getTrack(), plexTrack));
					metadata.setAlbum(plexTrack);
				}
				
				if (!metadata.getYear().equals(plexYear)) {
					logMessages.add(String.format("Year: %s -> %s", metadata.getYear(), plexYear));
					metadata.setAlbum(plexYear);
				}
				
				if (logMessages.isEmpty()) {
					System.out.println("Did not modify " + file.getAbsolutePath());
				} else {
					System.out.println("Modified " + file.getAbsolutePath());
					logMessages.forEach(System.out::println);
					modifiedTracks++;
				}
			} catch (IOException e) {
				e.printStackTrace();
				if (continueWithErrors) {
					continue;
				} else {
					System.exit(1);
				}
			}
		}
		
		System.out.println("Done saving metadata to disk!");
		System.out.printf("Processed %s tracks in %s seconds.\n", modifiedTracks, (System.currentTimeMillis() - metadataSavingStartTime) / 1000);
	}
	
	private static void saveDefaultConfig(File config) {
		try {
			FileUtils.copyOutOfJar(Main.class, "xyz/derkades/metadatasaver/default-config.yml", config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
