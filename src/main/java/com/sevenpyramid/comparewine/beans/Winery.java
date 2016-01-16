package com.sevenpyramid.comparewine.beans;

import java.util.List;

public class Winery {

	private List<String> WineryName;
	private List<WinePrice> winePrices;
	
	public List<String> getWineryName() {
		return WineryName;
	}
	public void setWineryName(List<String> wineryName) {
		WineryName = wineryName;
	}
	public List<WinePrice> getWinePrices() {
		return winePrices;
	}
	public void setWinePrices(List<WinePrice> winePrices) {
		this.winePrices = winePrices;
	}

	
}
