package xyz.derkades.metadatasaver.file.audio;

import java.io.File;

import xyz.derkades.metadatasaver.file.FileMetadata;

public abstract class MusicMetadata extends FileMetadata {

	public MusicMetadata(File file) {
		super(file);
	}

	public abstract String getAlbum();
	public abstract void setAlbum(String album);
	
	public abstract String getAlbumArtist();
	public abstract void setAlbumArtist(String albumArtist);
	
	public abstract String getArtist();
	public abstract void setArtist(String artist);
	
	public abstract String getTitle();
	public abstract void setTitle(String title);
	
	public abstract String getTrack();
	public abstract void setTrack(String track);
	
	public abstract String getYear();
	public abstract void setYear(String year);

}
