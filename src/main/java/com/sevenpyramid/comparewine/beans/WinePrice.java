package com.sevenpyramid.comparewine.beans;

public class WinePrice {

	private String wineName;
	private String wineVintage;
	private String price;
	private String priceUnit;
	private String bottleSize;
	private String origon;
	private String country;
	private String score;
	
	public WinePrice(){
		
	}
	
	
	public WinePrice(String wineName, String wineVintage, String price,
			String priceUnit, String bottleSize, String origon, String country,
			String score) {
		super();
		this.wineName = wineName;
		this.wineVintage = wineVintage;
		this.price = price;
		this.priceUnit = priceUnit;
		this.bottleSize = bottleSize;
		this.origon = origon;
		this.country = country;
		this.score = score;
	}


	public String getWineName() {
		return wineName;
	}
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}
	public String getWineVintage() {
		return wineVintage;
	}
	public void setWineVintage(String wineVintage) {
		this.wineVintage = wineVintage;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public String getBottleSize() {
		return bottleSize;
	}
	public void setBottleSize(String bottleSize) {
		this.bottleSize = bottleSize;
	}
	public String getOrigon() {
		return origon;
	}
	public void setOrigon(String origon) {
		this.origon = origon;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
	
	
	
}
