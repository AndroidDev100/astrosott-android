package com.dialog.dialoggo.beanModel.subscriptionmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Price{

	@SerializedName("price")
	private Price price;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("descriptions")
	private List<Object> descriptions;

	@SerializedName("objectType")
	private String objectType;

	@SerializedName("amount")
	private int amount;

	@SerializedName("currencySign")
	private String currencySign;

	@SerializedName("currency")
	private String currency;

	public void setPrice(Price price){
		this.price = price;
	}

	public Price getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setDescriptions(List<Object> descriptions){
		this.descriptions = descriptions;
	}

	public List<Object> getDescriptions(){
		return descriptions;
	}

	public void setObjectType(String objectType){
		this.objectType = objectType;
	}

	public String getObjectType(){
		return objectType;
	}

	public void setAmount(int amount){
		this.amount = amount;
	}

	public int getAmount(){
		return amount;
	}

	public void setCurrencySign(String currencySign){
		this.currencySign = currencySign;
	}

	public String getCurrencySign(){
		return currencySign;
	}

	public void setCurrency(String currency){
		this.currency = currency;
	}

	public String getCurrency(){
		return currency;
	}

	@Override
 	public String toString(){
		return 
			"Price{" + 
			"price = '" + price + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",descriptions = '" + descriptions + '\'' + 
			",objectType = '" + objectType + '\'' + 
			",amount = '" + amount + '\'' + 
			",currencySign = '" + currencySign + '\'' + 
			",currency = '" + currency + '\'' + 
			"}";
		}
}