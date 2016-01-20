package com.sevenpyramid.comparewine.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.sevenpyramid.comparewine.beans.FileAttribute;
import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.design.CompareResultDesign;
import com.sevenpyramid.comparewine.excel.FindEexcelHeader;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class CompareResultView extends CompareResultDesign implements View {

	public static final String NAME="COMPARERESULT";
	List<String> header;
	HashSet<String> selectFiles;
	ArrayList<FileAttribute> filesAttributes;
	
	public CompareResultView() {
		
		header=(List<String>) UI.getCurrent().getSession().getAttribute(Cons.SEL_HEADER);
		selectFiles = (HashSet<String>) UI.getCurrent().getSession().getAttribute(Cons.SELECTED_FILE);
		
		if(header!=null && selectFiles!=null){
			getHeaderAttribute();
		}else{
			Notification.show("Please slected Files and Header from excel File");
		}
		
	}

	private void getHeaderAttribute() {
		
		selectFiles.stream()
					.forEach(value->{
						System.out.println(value);
						FindEexcelHeader findExcelHeader = new FindEexcelHeader(value);
					});
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
