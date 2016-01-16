package com.sevenpyramid.comparewine.files;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.vaadin.server.VaadinService;

public class FindFilesUnderDirectory {

	String directory;
	String basePath=VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	public FindFilesUnderDirectory() {
	
	}
	
	public FindFilesUnderDirectory(String directory){
		this.directory=directory;
	
	}

	public List<File> getVaadinWebAbsoultPathFiles(){	
		
		return Arrays.asList(new File(getVaadinWebAbsoultPath()).listFiles());	
		
	}
	
	
	public String getVaadinWebAbsoultPath(){
		
		return basePath+this.directory;
		
	}
	
}
