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
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;


//THis is the SpreadSheetView
public class SpreadSheetView extends SpreadSheetMenuDesign implements View {

	public static final String NAME="SPREADSHEET";
	
	FindFilesUnderDirectory fd = new FindFilesUnderDirectory(Cons.DIRECTORY);
	List<File> files = fd.getVaadinWebAbsoultPathFiles();
	Spreadsheet spreadsheet = null;
	ArrayList<Cell> cellValueChangeList=null;
	File file;
	List<String> headerList;
	String headerValue;
	
	public SpreadSheetView() {

	}

	
	public SpreadSheetView(String value) {

		file = new File(fd.getVaadinWebAbsoultPath() + value);

		try {

			FindXssforHssfExcel xssforHssfExcel = new FindXssforHssfExcel(file);

			if (xssforHssfExcel.isXSSF() == true) {
				spreadsheet = new Spreadsheet();
				spreadsheet.setWorkbook(xssforHssfExcel.getXSSFWorkbook());
			
				spreadsheet.addCellValueChangeListener(event->{
								cellValueChangeList = new ArrayList<Cell>();
								for (CellReference cellReference : event.getChangedCells()) {
									cellValueChangeList.add(spreadsheet.getCell(cellReference));
								}
						});

				spreadsheet.setHeight("100%");
				spreadSheetLayout.addComponent(spreadsheet);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		saveButton.addClickListener(this::saveExcelFile);
		setHeaderButton.addClickListener(this::setHeader);
		matchHeaderButton.addClickListener(this::matchHeader);

	}
	
	public void setHeader(Button.ClickEvent event) {
		headerList = new ArrayList<String>();
		UI.getCurrent().getSession().setAttribute(Cons.SEL_HEADER,getSelectedCells());
			
	}

	@SuppressWarnings("unchecked")
	public void matchHeader(Button.ClickEvent event) {

		List<String> selHeader=(List<String>) UI.getCurrent().getSession().getAttribute(Cons.SEL_HEADER);
		Set<CellReference> cReferences=spreadsheet.getSelectedCellReferences();
		
		
		if(cReferences!=null){
			cReferences.stream()
						.forEach(reference->{
							//for each reference match the header that is pre set.
							if((spreadsheet.getCell(reference).getCellType()==Cell.CELL_TYPE_STRING)||
									(spreadsheet.getCell(reference).getCellType()==Cell.CELL_TYPE_BLANK)){
									
								
								Window w1 = new Window();
								ComboBox c1=new ComboBox("Match Header", selHeader);
								Button b1 = new Button("Comfirm");
								
								c1.addValueChangeListener(new ValueChangeListener() {
									
									@Override
									public void valueChange(ValueChangeEvent event) {
										
										spreadsheet.getCell(reference).setCellValue((String)event.getProperty().getValue());
										spreadsheet.refreshCells(spreadsheet.getCell(reference));
										
									}
								});
								
								b1.addClickListener(new ClickListener() {
									
									@Override
									public void buttonClick(ClickEvent event) {
										saveExcelFile(event);
										w1.close();
									}
								});
								HorizontalLayout hlayout=new HorizontalLayout();
								hlayout.addComponent(c1);
								hlayout.addComponent(b1);
								hlayout.setSpacing(true);
								hlayout.setMargin(true);
								w1.setContent(hlayout);
								w1.setHeight("200px");
								w1.setWidth("400px");
								w1.center();
								UI.getCurrent().addWindow(w1);
								
							}
						});
			

		}
		
		
	}
	
	public void getValue(ValueChangeEvent vEvent){
		
		headerValue=(String) vEvent.getProperty().getValue();
		System.out.println(headerValue);
	}
	

	@Override
	public void enter(ViewChangeEvent event) {

	}

	
	public void saveExcelFile(Button.ClickEvent event){
		
		try {
			
			
			if(cellValueChangeList!=null){
				spreadsheet.refreshCells(cellValueChangeList);
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

	
	private List<String> getSelectedCells() {
		//Select the column that is going to be header
		
		Set<CellReference> cReferences=spreadsheet.getSelectedCellReferences();	
		
		if (cReferences != null) {

			for (CellReference reference : cReferences) {

				if (spreadsheet.getCell(reference) != null) {

					if (spreadsheet.getCell(reference).getCellType() == Cell.CELL_TYPE_BLANK) {
						
					} else if (spreadsheet.getCell(reference).getCellType() == Cell.CELL_TYPE_ERROR) {
						
					} else if (spreadsheet.getCell(reference).getCellType() == Cell.CELL_TYPE_STRING) {
						headerList.add(spreadsheet.getCellValue(spreadsheet.getCell(reference)));
					}
				}
			}

		}
		
		return headerList;
	}
	
	
}
