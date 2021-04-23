package com.astro.sott.modelClasses.playbackContext;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("sources")
	private List<SourcesItem> sources;

	@SerializedName("messages")
	private List<MessagesItem> messages;

	@SerializedName("objectType")
	private String objectType;

	public List<SourcesItem> getSources(){
		return sources;
	}

	public List<MessagesItem> getMessages(){
		return messages;
	}

	public String getObjectType(){
		return objectType;
	}
}