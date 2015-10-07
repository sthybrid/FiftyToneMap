package com.sthybrid.fiftytonemap.myUI;

/**
 * 
 * @author ºúÑó
 * @date 2015/9/13
 *
 */

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
