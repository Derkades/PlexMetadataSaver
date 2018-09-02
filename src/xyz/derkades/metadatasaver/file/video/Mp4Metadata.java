package xyz.derkades.metadatasaver.file.video;

import java.io.File;

public class Mp4Metadata extends VideoMetadata {

	public Mp4Metadata(File file) {
		super(file);
	}

	@Override
	public String getTitle() {
		throw new UnsupportedOperationException(".mp4 files are not (yet) supported.");
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

}
