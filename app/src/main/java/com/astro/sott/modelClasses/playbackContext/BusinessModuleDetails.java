package com.astro.sott.modelClasses.playbackContext;

import com.google.gson.annotations.SerializedName;

public class BusinessModuleDetails{

	@SerializedName("objectType")
	private String objectType;

	public String getObjectType(){
		return objectType;
	}
}