package com.astro.sott.modelClasses.dmsResponse;

import com.google.gson.annotations.SerializedName;

public class TermsAndConditionsURL{

	@SerializedName("URL")
	private String uRL;

	public String getURL(){
		return uRL;
	}
}