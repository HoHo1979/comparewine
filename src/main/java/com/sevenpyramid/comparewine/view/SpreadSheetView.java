package com.sevenpyramid.comparewine.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;

import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.design.SpreadSheetMenuDesign;
import com.sevenpyramid.comparewine.excel.FindXssforHssfExcel;
import com.sevenpyramid.comparewine.files.FindFilesUnderDirectory;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.CellValueChangeEvent;
import com.vaadin.addon.spreadsheet.Spreadsheet.CellValueChangeListener;
import com.vaadin.addon.spreadsheet.action.SpreadsheetDefaultActionHandler;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ColorPicker;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;

//THis is the SpreadSheetView
public class SpreadSheetView extends SpreadSheetMenuDesign implements View {

	public static final String NAME="SPREADSHEET";
	
	FindFilesUnderDirectory fd = new FindFilesUnderDirectory(Cons.DIRECTORY);
	List<File> files = fd.getVaadinWebAbsoultPathFiles();
	Spreadsheet spreadsheet = null;
	ArrayList<Cell> cellValueChangeList=null;
	File file;
	
	public SpreadSheetView() {

		try {
				
				FindXssforHssfExcel xssforHssfExcel = new FindXssforHssfExcel(files.get(0));
				file=files.get(0);
				if(xssforHssfExcel.isXSSF()==true){
				spreadsheet = new Spreadsheet();
				spreadsheet.setWorkbook(xssforHssfExcel.getXSSFWorkbook());
				spreadsheet.addActionHandler(new SpreadsheetDefaultActionHandler());
				
				spreadsheet.addCellValueChangeListener(new CellValueChangeListener() {
					
					@Override
					public void onCellValueChange(CellValueChangeEvent event) {
						cellValueChangeList = new ArrayList<Cell>();
						
						for(CellReference cellReference:event.getChangedCells()){
						cellValueChangeList.add(spreadsheet.getCell(cellReference));
						}
					}
				});
				
				
				spreadsheet.setSizeFull();
				spreadSheetLayout.addComponent(spreadsheet);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveButton.addClickListener(this::saveExcelFile);

	}

	public SpreadSheetView(String value) {
		
		file = new File(fd.getVaadinWebAbsoultPath()+value);
		
		try {
			
			FindXssforHssfExcel xssforHssfExcel = new FindXssforHssfExcel(file);
			
			if(xssforHssfExcel.isXSSF()==true){
			spreadsheet = new Spreadsheet();
			spreadsheet.setWorkbook(xssforHssfExcel.getXSSFWorkbook());
			spreadsheet.addActionHandler(new SpreadsheetDefaultActionHandler());
			
			spreadsheet.addCellValueChangeListener(new CellValueChangeListener() {
				
				@Override
				public void onCellValueChange(CellValueChangeEvent event) {
					cellValueChangeList = new ArrayList<Cell>();
					
					for(CellReference cellReference:event.getChangedCells()){
					cellValueChangeList.add(spreadsheet.getCell(cellReference));
					}
				}
			});
			
			spreadsheet.setHeight("100%");
			spreadSheetLayout.addComponent(spreadsheet);

			}
			
	} catch (Exception e) {
		e.printStackTrace();
	}

	saveButton.addClickListener(this::saveExcelFile);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	
	public void saveExcelFile(Event event){
		
		try {
			
			
			if(cellValueChangeList!=null){
				spreadsheet.refreshCells(cellValueChangeList);
			}
			
			//Select the column that is going to be header
			Set<CellReference> cReferences=spreadsheet.getSelectedCellReferences();	
			
			if (cReferences != null) {

				for (CellReference reference : cReferences) {

					if (spreadsheet.getCell(reference) != null) {

						if (spreadsheet.getCell(reference).getCellType() == Cell.CELL_TYPE_BLANK) {
							
						} else if (spreadsheet.getCell(reference).getCellType() == Cell.CELL_TYPE_ERROR) {
							
						} else if (spreadsheet.getCell(reference).getCellType() == Cell.CELL_TYPE_STRING) {
							
						}
					}
				}

			}
			
			Workbook wb=spreadsheet.getWorkbook();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			wb.write(fileOutputStream);
			fileOutputStream.close();
			Notification.show("The file is successfully saved");
		
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	
	

	private HorizontalLayout createStyleToolbar() {
		HorizontalLayout toolbar = new HorizontalLayout();
		Button boldButton = new Button(FontAwesome.BOLD);
		boldButton.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				updateSelectedCellsBold();
			}
		});
		ColorPicker backgroundColor = new ColorPicker();
		backgroundColor.setCaption("Background Color");
		backgroundColor.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void colorChanged(ColorChangeEvent event) {
				updateSelectedCellsBackgroundColor(event.getColor());
			}
		});
		ColorPicker fontColor = new ColorPicker();
		fontColor.setCaption("Font Color");
		fontColor.addColorChangeListener(new ColorChangeListener() {
			@Override
			public void colorChanged(ColorChangeEvent event) {
				updateSelectedCellsFontColor(event.getColor());
			}
		});
		toolbar.addComponent(boldButton);
		toolbar.addComponent(backgroundColor);
		toolbar.addComponent(fontColor);
		return toolbar;
	}

	private void updateSelectedCellsBold() {
		
	}

	private void updateSelectedCellsBackgroundColor(Color newColor) {
		// TODO Auto-generated method stub
	}

	private void updateSelectedCellsFontColor(Color newColor) {
		// TODO Auto-generated method stub
	}
	
	
}
