package xyz.derkades.metadatasaver.file.audio;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import xyz.derkades.metadatasaver.Main;

/**
 * Now using a different library, see {@link xyz.derkades.metadatasaver.file.audio.Mp3MetadataJAudioTagger}
 */
@Deprecated
public class Mp3MetadataMp3agic extends MusicMetadata {

	private File file;
	private Mp3File mp3File;
	private ID3v2 id3v2;
	private ID3v1 id3v1;

	public Mp3MetadataMp3agic(File file) throws IOException {
		super(file);
		
		this.file = file;
		
		try {
			this.mp3File = new Mp3File(file);

			if (mp3File.hasId3v2Tag()) {
				id3v2 = mp3File.getId3v2Tag();
			} else {
				id3v2 = new ID3v24Tag();
			}
				
			if (mp3File.hasId3v1Tag()) {
				id3v1 = mp3File.getId3v1Tag();
			} else {
				id3v1 = new ID3v1Tag();
			}
		} catch (InvalidDataException | UnsupportedTagException | IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getAlbum() {
		if (Main.USE_ID3V2) {
			return id3v2.getAlbum();
		} else {
			return id3v1.getAlbum();
		}
	}

	@Override
	public void setAlbum(String album) {
		id3v1.setAlbum(album);
		id3v2.setAlbum(album);
	}

	/**
	 * Returns the album artist if the file has id3v2 tags, otherwise return the artist.
	 */
	@Override
	public String getAlbumArtist() {
		if (Main.USE_ID3V2) {
			return id3v2.getAlbumArtist();
		} else {
			// ID3v1 does not support album artist
			return id3v1.getArtist();
		}
	}

	@Override
	public void setAlbumArtist(String albumArtist) {
		id3v2.setAlbumArtist(albumArtist);
	}

	@Override
	public String getArtist() {
		if (Main.USE_ID3V2) {
			return id3v2.getArtist();
		} else {
			return id3v1.getArtist();
		}
	}

	@Override
	public void setArtist(String artist) {
		id3v2.setArtist(artist);
		id3v1.setArtist(artist);
	}

	@Override
	public String getTitle() {
		if (Main.USE_ID3V2) {
			return id3v2.getTitle();
		} else {
			return id3v1.getTitle();
		}
	}

	@Override
	public void setTitle(String title) {
		id3v1.setTitle(title);
		id3v2.setTitle(title);
	}

	@Override
	public String getTrack() {
		if (Main.USE_ID3V2) {
			return id3v2.getTrack();
		} else {
			return id3v1.getTrack();
		}
	}

	@Override
	public void setTrack(String track) {
		id3v1.setTrack(track);
		id3v2.setTrack(track);
	}

	@Override
	public String getYear() {
		if (Main.USE_ID3V2) {
			return id3v2.getYear();
		} else {
			return id3v1.getYear();
		}
	}

	@Override
	public void setYear(String year) {
		id3v1.setYear(year);
		id3v2.setYear(year);
	}
	
	@Override
	public void save() throws IOException {
		mp3File.setId3v1Tag(id3v1);
		mp3File.setId3v2Tag(id3v2);
		
		try {
			String newFile = file.getAbsolutePath() + ".new.mp3";
			mp3File.save(newFile);
		} catch (NotSupportedException e) {
			throw new IOException(e);
		}
	}

}
