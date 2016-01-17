package com.sevenpyramid.comparewine.view;


import java.util.Iterator;

import com.sevenpyramid.comparewine.design.MainDesign;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;

public class MainView extends MainDesign implements ViewDisplay {


	private Navigator navigator;
	
	public MainView() {
		
		navigator = new Navigator(UI.getCurrent(), scroll_panel);
		
		addViewWithButtonListener(AddExcelFileView.NAME,AddExcelFileView.class,menuButton1);
		addViewWithButtonListener(SelectFileView.NAME,SelectFileView.class , menuButton2);
		addViewWithButtonListener(CollaborateHeadView.NAME, CollaborateHeadView.class, menuButton3);
			
		if(navigator.getState().isEmpty()){
			navigator.addView(AddExcelFileView.NAME, AddExcelFileView.class);
			navigator.navigateTo(AddExcelFileView.NAME);
		}

	}


	private void addViewWithButtonListener(String name,
			Class<? extends View> viewClass, NativeButton menuButton) {
		
		navigator.addView(name, viewClass);
		menuButton.addClickListener(event-> toNaivagatorView(name));
		menuButton.setData(viewClass.getName());
		
	}

	private void toNaivagatorView(String name) {
	
		UI.getCurrent().getNavigator().navigateTo(name);
		
	}

	@Override
	public void showView(View view) {

		Iterator<Component> iterator=side_bar.iterator();
		while(iterator.hasNext()){
			Component component = iterator.next();
			if(component instanceof Button){
				System.out.println(((Button)component).getCaption());
			}
		}
		

	}

}
