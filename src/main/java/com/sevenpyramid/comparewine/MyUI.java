package com.sevenpyramid.comparewine;

import javax.servlet.annotation.WebServlet;

import com.sevenpyramid.comparewine.view.MainView;
import com.sevenpyramid.comparewine.view.SpreadSheetView;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 *
 */
@Theme("comparewinetheme")
@Widgetset("com.sevenpyramid.comparewine.MyAppWidgetset")
public class MyUI extends UI {
	

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
    	setContent(new MainView());
    	
    }


	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
