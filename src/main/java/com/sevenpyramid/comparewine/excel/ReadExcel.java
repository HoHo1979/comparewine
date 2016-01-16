package com.sevenpyramid.comparewine.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sevenpyramid.comparewine.beans.WinePrice;
import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.files.FindFilesUnderDirectory;
import com.vaadin.server.VaadinService;

public class ReadExcel {

	List<File> files;
	
	public ReadExcel() throws Exception {
		
			FindFilesUnderDirectory filesUnderDirectory = new FindFilesUnderDirectory(Cons.DIRECTORY);
			files=filesUnderDirectory.getVaadinWebAbsoultPathFiles();
			
			for(File f:files){
				FindXssforHssfExcel excelFile= new FindXssforHssfExcel(f);
				if(excelFile.isXSSF()==true){
					readXSSFFile(excelFile.getXSSFWorkbook());
				}else if(excelFile.isXSSF()==false){
					readHSSFFile(excelFile.getHSSFworkbook());
				}
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
		
			int rows=0;
			Sheet sheet = null;
			
			sheet = ws.getSheetAt(0);
			rows=sheet.getPhysicalNumberOfRows();	
		
			
			for(int r=0;r<rows;r++){
		
				Row row=sheet.getRow(r);
				
				if(row==null){
					continue;
				}
							
				
				int cells = row.getPhysicalNumberOfCells();
					
					for(int c=0;c<cells;c++){
						
				
						Cell cell=row.getCell(c);
						
						String value = null;
						
						if(cell==null){
							continue;
						}
						
						switch (cell.getCellType()) {

							case Cell.CELL_TYPE_FORMULA:
								value = "FORMULA value=" + cell.getCellFormula();
								break;

							case Cell.CELL_TYPE_NUMERIC:
								value = "NUMERIC value=" + cell.getNumericCellValue();
								break;

							case Cell.CELL_TYPE_STRING:
								String supplier=cell.getStringCellValue();
								
								if(supplier.contains(Cons.DUCLOT)){
									readDUCLOT(sheet,row,r,rows);
									c=cells;
									r=rows;
									
								}else if(supplier.contains(Cons.SOVEX_WOLTNER)){
									readSOVEX(sheet,row,r,rows);
									c=cells;
									r=rows;
									
								}else if(supplier.contains(Cons.LOUIS_VIALARD)){
									readLouis(sheet,row,r,rows);
									c=cells;
									r=rows;
								}
								
								break;

							default:
						}
						
						
					}
				
				}

	
		return null;
	}

	private void readLouis(Sheet sheet, Row row, int r, int rows) {

		for (int i = ++r; i < rows; i++) {

			row = sheet.getRow(i);

			if (row == null) {
				continue;
			}

			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {

				Cell cell = row.getCell(0);

				if (cell == null) {
					continue;
				}

				switch (cell.getCellType()) {

				case Cell.CELL_TYPE_STRING:
					String cell_1 = cell.getStringCellValue();
					System.out.println(cell_1);

					break;

				default:
				}

			}

		}
	}

	private void readSOVEX(Sheet sheet, Row row, int r, int rows) {
		
		
	}

	private void readDUCLOT(Sheet sheet, Row row, int r, int rows) {
		
		
	}
}


