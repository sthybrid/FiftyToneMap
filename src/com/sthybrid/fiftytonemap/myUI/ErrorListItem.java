package com.sthybrid.fiftytonemap.myUI;

public class ErrorListItem {

	private String name;
	private int errorPercent;
	
	public ErrorListItem(String name, int errorPercent){
		this.name = name;
		this.errorPercent = errorPercent;
	}
	public String getName() {
		return name;
	}
	public int getErrorPercent() {
		return errorPercent;
	}

}
