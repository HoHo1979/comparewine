package com.sevenpyramid.comparewine.excel;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.*;

import static java.util.stream.Stream.*;
import static java.util.stream.Collectors.*;

import org.apache.poi.hdgf.streams.StringsStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.format.CellElapsedFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;

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
	ArrayList<WinePrice> winePrices= new ArrayList<WinePrice>();
	
	
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
						.filter(x->sheet.getRow(x)!=null)
						.forEach(g->storeHeaderEntity(g,sheet));

				fileAttribute.setHeader(headerAttributes);	
				filesAttributes.add(fileAttribute);
				
				IntStream.range(0,rows)
						 .filter(x->sheet.getRow(x)!=null)
						 .forEach(x->readRow(x,sheet));					 
				
				winePrices.stream()
						.filter(x->x.getWineName()!=null)
						.limit(20)
						.forEach(x->{
							System.out.println(x.getWineName()+" "+x.getOrigon()+" "+x.getPrice()+" "+x.getWineVintage()+" "+x.getSupplier());
						});
				
				
						 
			return null;	
		}

	private void readRow(int x, Sheet sheet) {
		WinePrice winePrice = new WinePrice();
		
		IntStream.range(0, sheet.getRow(x).getPhysicalNumberOfCells())
				 .filter(c->sheet.getRow(x).getCell(c)!=null)
				 .filter(c->sheet.getRow(x).getCell(c).getCellType()!=Cell.CELL_TYPE_BLANK)
				 .forEach(c->{			 
						 getColumnIndicator(x, c, sheet, winePrice);
				 });
				 
		if(winePrice!=null){
		winePrices.add(winePrice);
		}
	}
	
	public String getColumnIndicator(int r,int c,Sheet sheet, WinePrice winePrice){
		
		
		winePrice.setSupplier(fileAttribute.getSupplier());
		
		headerAttributes.stream()
				.filter(x->x!=null)
				.filter(x->x.getHeaderColumn()==c)
				.filter(x->sheet.getRow(r).getCell(c).getCellType()==Cell.CELL_TYPE_STRING)
				.forEach(x->{
					
					switch (x.getHeaderName()) {
					case Cons.H_Name:
						
						winePrice.setWineName(sheet.getRow(r).getCell(c).getStringCellValue());
						break;

					case Cons.H_Price_Euro:
						winePrice.setPrice(sheet.getRow(r).getCell(c).getStringCellValue());
						
						break;
						
					case Cons.H_Orign:
						winePrice.setOrigon(sheet.getRow(r).getCell(c).getStringCellValue());
							
						break;
						
					case Cons.H_Vintage:
						winePrice.setWineVintage(sheet.getRow(r).getCell(c).getStringCellValue());
							
						break;
							
					case Cons.H_Bottle_Size:
						winePrice.setBottleSize(sheet.getRow(r).getCell(c).getStringCellValue());
						
						break;

					default:
						break;
					}
					
				});
		
		
		headerAttributes.stream()
		.filter(x->x.getHeaderColumn()==c)
		.filter(x->sheet.getRow(r).getCell(c).getCellType()==Cell.CELL_TYPE_NUMERIC)
		.forEach(x->{
			
			switch (x.getHeaderName()) {
	
			case Cons.H_Price_Euro:
				winePrice.setPrice(String.valueOf(sheet.getRow(r).getCell(c).getNumericCellValue()));	
				break;
				
			case Cons.H_Vintage:
				winePrice.setWineVintage(String.valueOf(sheet.getRow(r).getCell(c).getNumericCellValue()));
				break;
					
			case Cons.H_Bottle_Size:
				winePrice.setBottleSize(String.valueOf(sheet.getRow(r).getCell(c).getNumericCellValue()));
				
				break;

			default:
				break;
			}
			
		});

		return null;
	}
	

	private void findWine(int r, int c,Sheet sheet) {
		/*
		IntStream.range(r+1, sheet.getPhysicalNumberOfRows())
				 .filter(row->sheet.getRow(row)!=null)
				 .filter(row->sheet.getRow(row).getCell(c)!=null)
				 .filter(row->sheet.getRow(row).getCell(c).getCellType()==Cell.CELL_TYPE_STRING)
				 .limit(10)
				 .forEach(row->System.out.println(sheet.getRow(row).getCell(c).getStringCellValue()));
			*/	 
	}

	public void storeHeaderEntity(int g,Sheet sheet){
		
		IntStream.range(0, sheet.getRow(g).getPhysicalNumberOfCells())
		.filter(x->sheet.getRow(g).getCell(x)!=null)
		.filter(x->sheet.getRow(g).getCell(x).getCellType()==Cell.CELL_TYPE_STRING)
		.forEach(x->findHeader(g, x, sheet));
		
	}
	
	
	public void findHeader(int g, int cell,Sheet sheet){
		
		header.stream()
		  .filter(h->h.equals(sheet.getRow(g).getCell(cell).getStringCellValue()))
		  .forEach(h->storeHeadAttribute(g,cell,sheet));

	}
	
	
	public void storeHeadAttribute(int g, int cell, Sheet sheet){
		
		HeaderAttribute headerAttribute = new HeaderAttribute();
		headerAttribute.setHeaderName(sheet.getRow(g).getCell(cell).getStringCellValue());
		headerAttribute.setHeaderColumn(sheet.getRow(g).getCell(cell).getColumnIndex());
		headerAttribute.setHeaderRow(sheet.getRow(g).getCell(cell).getRowIndex());
		
		headerAttributes.add(headerAttribute);
	}


}
