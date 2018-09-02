package xyz.derkades.metadatasaver.file.video;

import java.io.File;

public class MkvMetadata extends VideoMetadata {

	public MkvMetadata(File file) {
		super(file);
	}

	@Override
	public String getTitle() {
		throw new UnsupportedOperationException("The matroska video container is not (yet) supported.");
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

}
