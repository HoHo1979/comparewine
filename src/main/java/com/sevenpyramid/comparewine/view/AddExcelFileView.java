package com.sevenpyramid.comparewine.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.List;
import java.util.function.Consumer;

import javax.validation.constraints.Min;

import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.design.AddExcelFileDesign;
import com.sevenpyramid.comparewine.excel.ReadExcel;
import com.sevenpyramid.comparewine.files.FindFilesUnderDirectory;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Upload.Receiver;

public class AddExcelFileView extends AddExcelFileDesign implements View,Receiver {

	public static final String NAME="ADDEXCEL";
	FindFilesUnderDirectory filesUnderDirectory;
	
	public AddExcelFileView() {
		//Find files under a selected directory \\WEB-INF\\wineprice\\
		filesUnderDirectory = new FindFilesUnderDirectory(Cons.DIRECTORY);
		
		refeshFileTree();
		
		uploadFile.setCaption("Add Excel File");
		uploadFile.setReceiver(this);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	public void refeshFileTree(){
		File folder = new File(filesUnderDirectory.getVaadinWebAbsoultPath());
		FilesystemContainer filesystemContainer = new FilesystemContainer(folder);
		
		Tree fileTree = new Tree("Excel　Files", filesystemContainer);
		
		filePanel.setContent(fileTree);
	}
	
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		FileOutputStream fileOutputStream=null;
		
			try {
				fileOutputStream = new FileOutputStream(
						new File(filesUnderDirectory.getVaadinWebAbsoultPath()+filename));
				Notification.show(filename+" is successfully uploaded");
				refeshFileTree();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		
		return fileOutputStream;
	}



}
