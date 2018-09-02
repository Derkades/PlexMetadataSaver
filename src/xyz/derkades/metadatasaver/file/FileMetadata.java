package xyz.derkades.metadatasaver.file;

import java.io.File;
import java.io.IOException;

public abstract class FileMetadata {
	
	private File file;
	
	public FileMetadata(File file){
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}
	
	public abstract void save() throws IOException;

}
