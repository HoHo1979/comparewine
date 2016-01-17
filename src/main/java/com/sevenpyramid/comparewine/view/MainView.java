package com.sevenpyramid.comparewine.view;


import java.util.Iterator;

import com.sevenpyramid.comparewine.design.MainDesign;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

public class MainView extends MainDesign implements ViewDisplay {


	private Navigator navigator;
	
	public MainView() {
		navigator = new Navigator(UI.getCurrent(), scroll_panel);
		navigator.addView(SpreadSheetView.NAME, SpreadSheetView.class);
		
		if(navigator.getState().isEmpty()){
			navigator.navigateTo(SpreadSheetView.NAME);
		}
		
		
		Iterator<Component> iterator=side_bar.iterator();
		while(iterator.hasNext()){
			Component component = iterator.next();
			if(component instanceof Button){
				System.out.println(((Button)component).getCaption());
			}
		}
		
	//	addButtonListenerWithView()
		
	}

	@Override
	public void showView(View view) {
		

	}

}
