package com.sevenpyramid.comparewine.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sevenpyramid.comparewine.beans.FileAttribute;
import com.sevenpyramid.comparewine.beans.WinePrice;
import com.sevenpyramid.comparewine.constant.Cons;
import com.sevenpyramid.comparewine.design.CompareResultDesign;
import com.sevenpyramid.comparewine.excel.FindEexcelWinePrice;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

public class CompareResultView extends CompareResultDesign implements View {

	public static final String NAME="COMPARERESULT";
	List<String> header;
	HashSet<String> selectFiles;
	ArrayList<FileAttribute> filesAttributes;
	
	public CompareResultView() {
		
		
	}

	private void getHeaderAttribute() {
		
			ArrayList<WinePrice> winePrices=new ArrayList<WinePrice>();
			
			for(String value: selectFiles){
			FindEexcelWinePrice findExcelWinePrice = new FindEexcelWinePrice(value);
				ArrayList<WinePrice> w1=findExcelWinePrice.getWinePrices();
				winePrices.addAll(w1);
			}
			
	
			ArrayList<WinePrice> tempPrices=winePrices;
			
			for(WinePrice x:tempPrices){
			
			 Optional<WinePrice> minWinePrice=winePrices.stream()
						.filter(y->x.getWineName().equals(y.getWineName())&&x.getWineVintage()==y.getWineVintage()&&x.getBottleSize()==y.getBottleSize())
						.min(Comparator.comparing((WinePrice w)->w.getPrice()));
				
			 	if(minWinePrice.isPresent()){
			  		minWinePrice.get().setIsCheapest("Cheapest");
			 		
			 	}else{
			 	    x.setIsCheapest("Cheapest");
			 	}
			}
			
			tempPrices=(ArrayList<WinePrice>) winePrices.stream()
						.sorted(Comparator.comparing((WinePrice x)->x.getWineName()).thenComparing(Comparator.comparing((WinePrice y)->y.getBottleSize())))
						.collect(Collectors.toList());
			
				
			if(tempPrices!=null){
				resultGrid.setCaption("Compare Price");
				BeanItemContainer<WinePrice> beanItemContainer =new BeanItemContainer<WinePrice>(WinePrice.class);
				beanItemContainer.addAll(tempPrices);
				resultGrid.setContainerDataSource(beanItemContainer);
				resultGrid.setColumns("wineName","wineVintage","price","bottleSize","supplier","isCheapest");
				resultGrid.setImmediate(true);
			}
			
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		header=(List<String>) UI.getCurrent().getSession().getAttribute(Cons.SEL_HEADER);
		selectFiles = (HashSet<String>) UI.getCurrent().getSession().getAttribute(Cons.SELECTED_FILE);
		
		if(header!=null && selectFiles!=null){
			getHeaderAttribute();
		}else{
			Notification.show("Please slected Files and Header from excel File");
		}

	}

}
