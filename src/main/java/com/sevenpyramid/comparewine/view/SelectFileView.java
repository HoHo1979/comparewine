package com.sevenpyramid.comparewine.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.resources.css.ast.CssProperty.Value;
import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.design.SelectFileDesign;
import com.sevenpyramid.comparewine.files.FindFilesUnderDirectory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;

public class SelectFileView extends SelectFileDesign implements View {

	public static final String NAME="SELECTFILE";
	
	Set<String> selectedFiles;
	
	public SelectFileView() {
		selectedGrid.setCaption("Selected File");
		
		selectedFiles = new HashSet<String>();
		
		FindFilesUnderDirectory filesUnderDirectory = new FindFilesUnderDirectory(Cons.DIRECTORY);
		File folder = new File(filesUnderDirectory.getVaadinWebAbsoultPath());
		FilesystemContainer fileContainer = new FilesystemContainer(folder);
		
		Collection<String> sfile=fileContainer.getContainerPropertyIds();

		fileTreeTable.setContainerDataSource(fileContainer);
		fileTreeTable.setVisibleColumns("Name");
		fileTreeTable.setSelectable(true);
		fileTreeTable.setMultiSelect(true);
		
		fileTreeTable.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				
			
				selectedFiles.add((String) event.getItem().getItemProperty(event.getPropertyId()).getValue());
				
				IndexedContainer container = new IndexedContainer();
						
				container.addContainerProperty("Name", String.class,"");
				
				selectedFiles.stream()
							.forEach(value->{ 
								Item item=container.addItem(value);
								item.getItemProperty("Name").setValue(value);
							});
				

				selectedGrid.setContainerDataSource(container);
				selectedGrid.setEditorEnabled(true);
				selectedGrid.setImmediate(true);
				
			}
		});
		
	}
	
	
	

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
