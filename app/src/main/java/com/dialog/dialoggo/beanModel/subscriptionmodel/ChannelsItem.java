package com.dialog.dialoggo.beanModel.subscriptionmodel;

import com.google.gson.annotations.SerializedName;

public class ChannelsItem{

	@SerializedName("id")
	private int id;

	@SerializedName("objectType")
	private String objectType;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setObjectType(String objectType){
		this.objectType = objectType;
	}

	public String getObjectType(){
		return objectType;
	}

	@Override
 	public String toString(){
		return 
			"ChannelsItem{" + 
			"id = '" + id + '\'' + 
			",objectType = '" + objectType + '\'' + 
			"}";
		}
}