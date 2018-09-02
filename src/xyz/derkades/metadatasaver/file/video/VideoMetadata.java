package xyz.derkades.metadatasaver.file.video;

import java.io.File;

import xyz.derkades.metadatasaver.file.FileMetadata;

public abstract class VideoMetadata extends FileMetadata {
	
	public VideoMetadata(File file) {
		super(file);
	}
	
	public abstract String getTitle();

}
