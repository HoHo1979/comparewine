package com.sevenpyramid.comparewine.beans;

public class HeaderAttribute {

	private String headerName;
	private int headerRow;
	private int headerColumn;
	
	public HeaderAttribute() {
		// TODO Auto-generated constructor stub
	}

	public int getHeaderColumn() {
		return headerColumn;
	}

	public void setHeaderColumn(int headerColumn) {
		this.headerColumn = headerColumn;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public int getHeaderRow() {
		return headerRow;
	}

	public void setHeaderRow(int headerRow) {
		this.headerRow = headerRow;
	}

}
