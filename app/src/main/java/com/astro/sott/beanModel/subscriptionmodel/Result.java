package com.astro.sott.beanModel.subscriptionmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("objects")
	private List<ObjectsItem> objects;

	@SerializedName("totalCount")
	private int totalCount;

	@SerializedName("objectType")
	private String objectType;

	public void setObjects(List<ObjectsItem> objects){
		this.objects = objects;
	}

	public List<ObjectsItem> getObjects(){
		return objects;
	}

	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}

	public int getTotalCount(){
		return totalCount;
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
			"Result{" + 
			"objects = '" + objects + '\'' + 
			",totalCount = '" + totalCount + '\'' + 
			",objectType = '" + objectType + '\'' + 
			"}";
		}
}