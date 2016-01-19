package com.sevenpyramid.comparewine.view;


import java.util.Iterator;

import com.sevenpyramid.comparewine.design.MainDesign;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;

public class MainView extends MainDesign implements ViewDisplay{

	private static final String STYLE_SELECTED = "selected";
	private Navigator navigator;
	
	public MainView() {
		
		navigator = new Navigator(UI.getCurrent(),(ViewDisplay)this);
		
		//add three views into the navigator
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
		menuButton.addClickListener(event->toNaivagatorView(event,name));
		menuButton.setData(viewClass.getName());
		
	}

	private void toNaivagatorView(Button.ClickEvent event,String name) {
	
		UI.getCurrent().getNavigator().navigateTo(name);

	}

	
	//Adjust the side bar , button style
	private void adjustStyleByData(Component component, String data) {
        if (component instanceof Button) {
            if (data != null && data.equals(((Button) component).getData())) {
               	((Button)component).addStyleName(STYLE_SELECTED);
            } else {
            	((Button)component).removeStyleName(STYLE_SELECTED);
            }
        }
    }

	//Override ViewDisplay showView from Navigator.ComponentContainerViewDisplay class.
	@Override
	public void showView(View view) {
		
		if (view instanceof Component) {
            scroll_panel.setContent((Component) view);
            Iterator<Component> it = side_bar.iterator();
            while (it.hasNext()) {
                adjustStyleByData(it.next(), view.getClass().getName());
                
            }
        } else {
            throw new IllegalArgumentException("View is not a Component");
        }
	}


}
