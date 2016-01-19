package com.sevenpyramid.comparewine.view;

import java.util.HashSet;
import java.util.Set;

import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.design.CollaborateHeadDesign;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class CollaborateHeadView extends CollaborateHeadDesign implements View {

	public static final String NAME="COLLABORATEHEAD";
	
	@SuppressWarnings("unchecked")
	public CollaborateHeadView() {
	
		HashSet<String> selectedFile=(HashSet<String>) UI.getCurrent().getSession().getAttribute(Cons.SELECTED_FILE);
		
		if(selectedFile!=null){
			
			selectedFile.stream()
					.forEach(value->excelTabSheet.addTab(new SpreadSheetView(value), value));

			excelTabSheet.setImmediate(true);
		}else{
			Notification.show("Please Select files");
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		

	}

}
