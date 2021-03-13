package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class AssetHistoryDays{

	@SerializedName("Days")
	private String days;

	public String getDays(){
		return days;
	}
}