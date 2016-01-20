package com.sevenpyramid.comparewine.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.*;
import static java.util.stream.Stream.*;

import static java.util.stream.Collectors.*;

import org.apache.poi.hdgf.streams.StringsStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellElapsedFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gwt.http.client.Header;
import com.sevenpyramid.comparewine.beans.FileAttribute;
import com.sevenpyramid.comparewine.beans.HeaderAttribute;
import com.sevenpyramid.comparewine.beans.WinePrice;
import com.sevenpyramid.comparewine.constant.Cons;
import com.vaadin.sass.internal.tree.ReturnNode;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;


public class FindEexcelHeader{
	
	FileAttribute fileAttribute=new FileAttribute();
	List<String> header=(List<String>) UI.getCurrent().getSession().getAttribute(Cons.SEL_HEADER);
	ArrayList<HeaderAttribute> headerAttributes = new ArrayList<HeaderAttribute>();
	ArrayList<FileAttribute> filesAttributes = new ArrayList<FileAttribute>(); 
	
	public FindEexcelHeader(String value) {
			
			String basePath=VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
			File file = new File(basePath+Cons.DIRECTORY+value);
			FileAttribute fileAttribute = new FileAttribute();
			fileAttribute.setFile(file);
			fileAttribute.setFileName(file.getName());
			
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				FindXssforHssfExcel excelFile= new FindXssforHssfExcel(file);
				if(excelFile.isXSSF()==true){
					readXSSFFile(excelFile.getXSSFWorkbook());
				}else if(excelFile.isXSSF()==false){
					readHSSFFile(excelFile.getHSSFworkbook());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			
			
			
			
	}

	private void readHSSFFile(HSSFWorkbook hssFworkbook) {
		readExcelFile(hssFworkbook);
		
	}

	private void readXSSFFile(XSSFWorkbook xssfWorkbook) {
		readExcelFile(xssfWorkbook);
		
	}
	
	//Read the execl file that is given.
	private List<WinePrice> readExcelFile(Workbook ws) {
			
				Sheet sheet = ws.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();	
					
					IntStream.range(0, rows)
						.filter(g->sheet.getRow(g)!=null)
						.forEach(g->storeHeaderEntity(g,sheet));
						
						
				fileAttribute.setHeader(headerAttributes);	
				filesAttributes.add(fileAttribute);

				
				
				
			return null;	
		}

	public void storeHeaderEntity(int g,Sheet sheet){
		
		IntStream.range(0, sheet.getRow(g).getPhysicalNumberOfCells())
		.filter(x->sheet.getRow(g).getCell(x)!=null)
		.filter(x->sheet.getRow(g).getCell(x).getCellType()==Cell.CELL_TYPE_STRING)
		.forEach(cell->findHeader(g, cell, sheet));
		
	}
	
	
	public void findHeader(int g, int cell,Sheet sheet){
		
		header.stream()
		  .filter(h->h.equals(sheet.getRow(g).getCell(cell).getStringCellValue()))
		  .forEach(y->storeHeadAttribute(g,cell,sheet));
		

	}
	
	
	public void storeHeadAttribute(int g, int cell, Sheet sheet){
		
		HeaderAttribute headerAttribute = new HeaderAttribute();
		headerAttribute.setHeaderName(sheet.getRow(g).getCell(cell).getStringCellValue());
		headerAttribute.setHeaderColumn(sheet.getRow(g).getCell(cell).getColumnIndex());
		headerAttribute.setHeaderRow(sheet.getRow(g).getCell(cell).getRowIndex());
		
		headerAttributes.add(headerAttribute);
	}

}
