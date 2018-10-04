package xyz.derkades.metadatasaver.file.audio;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class Mp3MetadataJAudioTagger extends MusicMetadata {

	private AudioFile audioFile;
	private Tag tag;
	
	public Mp3MetadataJAudioTagger(File file) throws IOException {
		super(file);
		
		try {
			audioFile = AudioFileIO.read(file);
			tag = audioFile.getTag();
		} catch (CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getAlbum() {
		return tag.getFirst(FieldKey.ALBUM);
	}

	@Override
	public void setAlbum(String album) {
		try {
			tag.setField(FieldKey.ALBUM, album);
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAlbumArtist() {
		return tag.getFirst(FieldKey.ALBUM_ARTIST);
	}

	@Override
	public void setAlbumArtist(String albumArtist) {
		try {
			tag.setField(FieldKey.ALBUM_ARTIST, albumArtist);
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getArtist() {
		return tag.getFirst(FieldKey.ARTIST);
	}

	@Override
	public void setArtist(String artist) {
		try {
			tag.setField(FieldKey.ARTIST, artist);
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return tag.getFirst(FieldKey.TITLE);
	}

	@Override
	public void setTitle(String title) {
		try {
			tag.setField(FieldKey.TITLE, title);
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTrack() {
		return tag.getFirst(FieldKey.TRACK);
	}

	@Override
	public void setTrack(String track) {
		try {
			tag.setField(FieldKey.TRACK, track);
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getYear() {
		return tag.getFirst(FieldKey.YEAR);
	}

	@Override
	public void setYear(String year) {
		try {
			tag.setField(FieldKey.YEAR, year);
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() throws IOException {
		try {
			audioFile.setTag(tag);
			AudioFileIO.write(audioFile);
		} catch (CannotWriteException e) {
			throw new IOException(e);
		}
	}

}
