package com.astro.sott.modelClasses.playbackContext;

import com.google.gson.annotations.SerializedName;

public class MessagesItem{

	@SerializedName("code")
	private String code;

	@SerializedName("message")
	private String message;

	@SerializedName("objectType")
	private String objectType;

	public String getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}

	public String getObjectType(){
		return objectType;
	}
}