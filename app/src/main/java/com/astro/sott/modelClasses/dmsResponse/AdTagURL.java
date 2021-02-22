package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdTagURL {
	@SerializedName("URL")
	@Expose
	private String uRL;

	public String getURL(){
		return uRL;
	}
}
