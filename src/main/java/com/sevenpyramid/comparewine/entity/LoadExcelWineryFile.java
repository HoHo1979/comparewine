package com.sevenpyramid.comparewine.entity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.sevenpyramid.comparewine.constant.Cons;
import com.vaadin.server.VaadinService;

public class LoadExcelWineryFile {

	public LoadExcelWineryFile() {
		try {
			findFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void findFile() throws FileNotFoundException,IOException {
		
		String basePath=VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		String wineryFilePath=basePath+Cons.DIRECTORY+"Winery.xls";
		String wineFilePath=basePath+Cons.DIRECTORY+"09 23 15 Grand Cru Price List.xls";
		Path path = Paths.get(wineryFilePath);
		
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(wineFilePath)))) {

			HSSFWorkbook wb = new HSSFWorkbook(bufferedInputStream);
			HSSFSheet sheet = wb.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();

			
			
			
		} catch (Exception e) {

		}
		
	}


	

}
