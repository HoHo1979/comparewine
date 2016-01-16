package com.sevenpyramid.comparewine.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FindXssforHssfExcel {

	File file;
	boolean XSSF=false;
	
	public FindXssforHssfExcel() {
		
	}
	
	public FindXssforHssfExcel(File f) throws Exception{
		this.file = f;
			if(f.getName().contains(".xls")){
				indetifyFile();
			}else{
				throw new Exception("Your given file is not an excel file");
			}
	}

	private void indetifyFile() {
		if(file.getName().contains(".xlsx")){
			this.XSSF=true;
		}else{
			this.XSSF=false;
		}
	}
	
	//Return a HSSFWorkBook
	public HSSFWorkbook getHSSFworkbook(){
		
		HSSFWorkbook wb=null;
		if (XSSF == false) {
			try (FileInputStream inputStream = new FileInputStream(file)) {

				wb = new HSSFWorkbook(inputStream);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wb;
	}
	
	//Return a XSSFWorkbook
	public XSSFWorkbook getXSSFWorkbook(){
		
		XSSFWorkbook wb=null;
		if (XSSF == true) {
			try (FileInputStream inputStream = new FileInputStream(file)) {
				
				wb = new XSSFWorkbook(inputStream);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return wb;
	}
	
	public boolean isXSSF() {
		return this.XSSF;
	}


}
