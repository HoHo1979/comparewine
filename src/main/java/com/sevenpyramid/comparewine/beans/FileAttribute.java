package com.sevenpyramid.comparewine.beans;

import java.io.File;
import java.util.ArrayList;

public class FileAttribute {

	private File file;
	private String fileName;
	private String supplier;
	private ArrayList<HeaderAttribute> headerArrayList;
	
	public FileAttribute() {
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public ArrayList<HeaderAttribute> getHeader() {
		return headerArrayList;
	}

	public void setHeader(ArrayList<HeaderAttribute> header) {
		this.headerArrayList = header;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}


}
