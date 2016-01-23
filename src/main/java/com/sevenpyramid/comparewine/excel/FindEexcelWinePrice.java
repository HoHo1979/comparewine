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
import com.sevenpyramid.comparewine.utility.WineBottleSize;
import com.vaadin.sass.internal.tree.ReturnNode;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;


public class FindEexcelWinePrice{
	
	FileAttribute fileAttribute=new FileAttribute();
	List<String> header=(List<String>) UI.getCurrent().getSession().getAttribute(Cons.SEL_HEADER);
	ArrayList<HeaderAttribute> headerAttributes = new ArrayList<HeaderAttribute>();
	ArrayList<FileAttribute> filesAttributes = new ArrayList<FileAttribute>(); 
	ArrayList<WinePrice> winePrices= new ArrayList<WinePrice>();
  

	WinePrice previouWinePrice;
	
	public FindEexcelWinePrice(String value) {
			
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
	
	public ArrayList<WinePrice> getWinePrices() {
		return winePrices;
	}
	
	//Read the execl file that is given.
	private List<WinePrice> readExcelFile(Workbook ws) {
			
		
				Sheet sheet = ws.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();	
				System.out.println("rows is"+rows);
				
				//Iterat each row to find header and store it in headerAttribute
				IntStream.range(0, rows)
						 .filter(row->sheet.getRow(row)!=null)
					   	 .forEach(row->storeHeaderEntity(row,sheet));

				//Set the header of this file
				fileAttribute.setHeader(headerAttributes);	
				filesAttributes.add(fileAttribute);
				
				//Iterat each row and store the row into a WinePrice object
				IntStream.range(0,rows)
						 .filter(x->sheet.getRow(x)!=null)
						 .forEach(x->readRow(x,sheet));					 
				
				
				
						 
			return null;	
		}

	private void readRow(int x, Sheet sheet) {
		
		WinePrice winePrice = new WinePrice();
		//
		winePrice.setSupplier(fileAttribute.getSupplier());
		
		StoreWinePriceByCell(x,sheet,winePrice);
		
	
		if(winePrice!=null){
				
				if(winePrice.getWineName()==null&&previouWinePrice!=null){
					winePrice.setWineName(previouWinePrice.getWineName());
				}
				
				if(winePrice.getWineName()!=null && winePrice.getPrice()!=0){
				winePrices.add(winePrice);
				previouWinePrice = winePrice;
				}
		}	

	}
	
	
	private void StoreWinePriceByCell(int x, Sheet sheet, WinePrice winePrice) {
		
			headerAttributes.stream()
					.filter(h->h.getHeaderName().equals(Cons.H_Name))
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn())!=null)
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn()).getCellType()==Cell.CELL_TYPE_STRING)
					.forEach(h->{
						
						String sTemp = sheet.getRow(x).getCell(h.getHeaderColumn()).getStringCellValue();
						sTemp=sTemp.toUpperCase();
						
						if(sTemp.contains("CH ")){
							sTemp=sTemp.replace("CH ","");
						}else if(sTemp.contains("CHATEAU ")){
							sTemp=sTemp.replace("CHATEAU ", "");
						}
						
						sTemp=sTemp.trim();
						
					    winePrice.setWineName(sTemp);
					
					});
			
			headerAttributes.stream()
					.filter(h->h.getHeaderName().equals(Cons.H_Price_Euro))
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn())!=null)
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn()).getCellType()==Cell.CELL_TYPE_NUMERIC)
					.forEach(h->winePrice.setPrice(sheet.getRow(x).getCell(h.getHeaderColumn()).getNumericCellValue()));
			//find the Orign and store into winePrice class
			headerAttributes.stream()
					.filter(h->h.getHeaderName().equals(Cons.H_Orign))
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn())!=null)
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn()).getCellType()==Cell.CELL_TYPE_STRING)			
					.forEach(h->winePrice.setOrigon(sheet.getRow(x).getCell(h.getHeaderColumn()).getStringCellValue()));
					
			//find the Vintage and store into winePrice class
			headerAttributes.stream()
					.filter(h->h.getHeaderName().equals(Cons.H_Vintage))
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn())!=null)
					.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn()).getCellType()==Cell.CELL_TYPE_NUMERIC)
					.forEach(h->winePrice.setWineVintage(sheet.getRow(x).getCell(h.getHeaderColumn()).getNumericCellValue()));

			//Find the bottle size and match either string representation to double by calling WineBottleSize
			headerAttributes.stream()
							.filter(h->h.getHeaderName().equals(Cons.H_Bottle_Size))
							.filter(h->sheet.getRow(x).getCell(h.getHeaderColumn())!=null)
							.forEach(h->{
								
								Cell cell=sheet.getRow(x).getCell(h.getHeaderColumn());
								double size=0;
								WineBottleSize wBottleSize = new WineBottleSize();
									if(cell.getCellType()==Cell.CELL_TYPE_STRING){
										size=wBottleSize.getWineBottleSize(cell.getStringCellValue());
									}else if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC){
										size=cell.getNumericCellValue();
									}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
										size=0.75;
									}
									
								winePrice.setBottleSize(size);
									
							});

			
			
	}

	public void storeHeaderEntity(int row,Sheet sheet){
		
		IntStream.range(0, sheet.getRow(row).getPhysicalNumberOfCells())
		.filter(cell->sheet.getRow(row).getCell(cell)!=null)
		.filter(cell->sheet.getRow(row).getCell(cell).getCellType()==Cell.CELL_TYPE_STRING)
		.forEach(cell->findHeader(row, cell, sheet));
		
	}
	
	
	public void findHeader(int row, int cell,Sheet sheet){
		
		Optional<String> supplierName=Stream.of(Cons.DUCLOT,Cons.LOUIS_VIALARD,Cons.SOVEX_WOLTNER,Cons.TWINS,Cons.TW_CHATEAU)
			  .filter(x->sheet.getRow(row).getCell(cell).getStringCellValue().contains(x))
			  .findAny();	
		
		if(supplierName.isPresent()){
			fileAttribute.setSupplier(supplierName.get());
		}
		
		header.stream()
		  .filter(h->h.equals(sheet.getRow(row).getCell(cell).getStringCellValue()))
		  .forEach(h->storeHeadAttribute(row,cell,sheet));

	}
	
	
	public void storeHeadAttribute(int g, int cell, Sheet sheet){
		
		HeaderAttribute headerAttribute = new HeaderAttribute();
		headerAttribute.setHeaderName(sheet.getRow(g).getCell(cell).getStringCellValue());
		headerAttribute.setHeaderColumn(sheet.getRow(g).getCell(cell).getColumnIndex());
		headerAttribute.setHeaderRow(sheet.getRow(g).getCell(cell).getRowIndex());
			
		headerAttributes.add(headerAttribute);
	}


}
