package com.astro.sott.modelClasses.playbackContext;

import com.google.gson.annotations.SerializedName;

public class DrmItem{

	@SerializedName("licenseURL")
	private String licenseURL;

	@SerializedName("scheme")
	private String scheme;

	@SerializedName("objectType")
	private String objectType;

	public String getLicenseURL(){
		return licenseURL;
	}

	public String getScheme(){
		return scheme;
	}

	public String getObjectType(){
		return objectType;
	}
}