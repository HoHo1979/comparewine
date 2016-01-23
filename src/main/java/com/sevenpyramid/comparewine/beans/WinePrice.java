package com.sevenpyramid.comparewine.beans;

public class WinePrice {

	private String wineName;
	private double wineVintage;
	private double price;
	private String priceUnit;
	private double bottleSize;
	private String origon;
	private String country;
	private String score;
	private String quantity;
	private String supplier;
	private String isCheapest="";

	public WinePrice(){
		
	}
	
	public String getWineName() {
		return wineName;
	}
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}
	public double getWineVintage() {
		return wineVintage;
	}
	public void setWineVintage(double wineVintage) {
		this.wineVintage = wineVintage;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}
	public double getBottleSize() {
		return bottleSize;
	}
	public void setBottleSize(double bottleSize) {
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


	public String getQuantity() {
		return quantity;
	}


	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}


	public String getSupplier() {
		return supplier;
	}


	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getIsCheapest() {
		return isCheapest;
	}

	public void setIsCheapest(String isCheapest) {
		this.isCheapest = isCheapest;
	}


	
}
