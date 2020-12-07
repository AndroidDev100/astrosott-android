package com.dialog.dialoggo.beanModel.subscriptionmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ObjectsItem{

	@SerializedName("pricePlanIds")
	private String pricePlanIds;

	@SerializedName("endDate")
	private long endDate;

	@SerializedName("description")
	private String description;

	@SerializedName("userTypes")
	private List<Object> userTypes;

	@SerializedName("mediaId")
	private int mediaId;

	@SerializedName("waiverPeriod")
	private int waiverPeriod;

	@SerializedName("objectType")
	private String objectType;

	@SerializedName("gracePeriodMinutes")
	private int gracePeriodMinutes;

	@SerializedName("isInfiniteRenewal")
	private boolean isInfiniteRenewal;

	@SerializedName("isCancellationBlocked")
	private boolean isCancellationBlocked;

	@SerializedName("isRenewable")
	private boolean isRenewable;

	@SerializedName("price")
	private Price price;

	@SerializedName("couponsGroups")
	private List<Object> couponsGroups;

	@SerializedName("id")
	private String id;

	@SerializedName("productCodes")
	private List<Object> productCodes;

	@SerializedName("renewalsNumber")
	private int renewalsNumber;

	@SerializedName("isWaiverEnabled")
	private boolean isWaiverEnabled;

	@SerializedName("householdLimitationsId")
	private int householdLimitationsId;

	@SerializedName("externalId")
	private String externalId;

	@SerializedName("fileTypes")
	private List<Object> fileTypes;

	@SerializedName("viewLifeCycle")
	private int viewLifeCycle;

	@SerializedName("maxViewsNumber")
	private int maxViewsNumber;

	@SerializedName("premiumServices")
	private List<Object> premiumServices;

	@SerializedName("dependencyType")
	private String dependencyType;

	@SerializedName("channels")
	private List<ChannelsItem> channels;

	@SerializedName("name")
	private String name;

	@SerializedName("prorityInOrder")
	private int prorityInOrder;

	@SerializedName("startDate")
	private int startDate;

	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public void setPricePlanIds(String pricePlanIds){
		this.pricePlanIds = pricePlanIds;
	}

	public String getPricePlanIds(){
		return pricePlanIds;
	}

	public void setEndDate(long endDate){
		this.endDate = endDate;
	}

	public long getEndDate(){
		return endDate;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setUserTypes(List<Object> userTypes){
		this.userTypes = userTypes;
	}

	public List<Object> getUserTypes(){
		return userTypes;
	}

	public void setMediaId(int mediaId){
		this.mediaId = mediaId;
	}

	public int getMediaId(){
		return mediaId;
	}

	public void setWaiverPeriod(int waiverPeriod){
		this.waiverPeriod = waiverPeriod;
	}

	public int getWaiverPeriod(){
		return waiverPeriod;
	}

	public void setObjectType(String objectType){
		this.objectType = objectType;
	}

	public String getObjectType(){
		return objectType;
	}

	public void setGracePeriodMinutes(int gracePeriodMinutes){
		this.gracePeriodMinutes = gracePeriodMinutes;
	}

	public int getGracePeriodMinutes(){
		return gracePeriodMinutes;
	}

	public void setIsInfiniteRenewal(boolean isInfiniteRenewal){
		this.isInfiniteRenewal = isInfiniteRenewal;
	}

	public boolean isIsInfiniteRenewal(){
		return isInfiniteRenewal;
	}

	public void setIsCancellationBlocked(boolean isCancellationBlocked){
		this.isCancellationBlocked = isCancellationBlocked;
	}

	public boolean isIsCancellationBlocked(){
		return isCancellationBlocked;
	}

	public void setIsRenewable(boolean isRenewable){
		this.isRenewable = isRenewable;
	}

	public boolean isIsRenewable(){
		return isRenewable;
	}

	public void setPrice(Price price){
		this.price = price;
	}

	public Price getPrice(){
		return price;
	}

	public void setCouponsGroups(List<Object> couponsGroups){
		this.couponsGroups = couponsGroups;
	}

	public List<Object> getCouponsGroups(){
		return couponsGroups;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setProductCodes(List<Object> productCodes){
		this.productCodes = productCodes;
	}

	public List<Object> getProductCodes(){
		return productCodes;
	}

	public void setRenewalsNumber(int renewalsNumber){
		this.renewalsNumber = renewalsNumber;
	}

	public int getRenewalsNumber(){
		return renewalsNumber;
	}

	public void setIsWaiverEnabled(boolean isWaiverEnabled){
		this.isWaiverEnabled = isWaiverEnabled;
	}

	public boolean isIsWaiverEnabled(){
		return isWaiverEnabled;
	}

	public void setHouseholdLimitationsId(int householdLimitationsId){
		this.householdLimitationsId = householdLimitationsId;
	}

	public int getHouseholdLimitationsId(){
		return householdLimitationsId;
	}

	public void setExternalId(String externalId){
		this.externalId = externalId;
	}

	public String getExternalId(){
		return externalId;
	}

	public void setFileTypes(List<Object> fileTypes){
		this.fileTypes = fileTypes;
	}

	public List<Object> getFileTypes(){
		return fileTypes;
	}

	public void setViewLifeCycle(int viewLifeCycle){
		this.viewLifeCycle = viewLifeCycle;
	}

	public int getViewLifeCycle(){
		return viewLifeCycle;
	}

	public void setMaxViewsNumber(int maxViewsNumber){
		this.maxViewsNumber = maxViewsNumber;
	}

	public int getMaxViewsNumber(){
		return maxViewsNumber;
	}

	public void setPremiumServices(List<Object> premiumServices){
		this.premiumServices = premiumServices;
	}

	public List<Object> getPremiumServices(){
		return premiumServices;
	}

	public void setDependencyType(String dependencyType){
		this.dependencyType = dependencyType;
	}

	public String getDependencyType(){
		return dependencyType;
	}

	public void setChannels(List<ChannelsItem> channels){
		this.channels = channels;
	}

	public List<ChannelsItem> getChannels(){
		return channels;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setProrityInOrder(int prorityInOrder){
		this.prorityInOrder = prorityInOrder;
	}

	public int getProrityInOrder(){
		return prorityInOrder;
	}

	public void setStartDate(int startDate){
		this.startDate = startDate;
	}

	public int getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"ObjectsItem{" + 
			"pricePlanIds = '" + pricePlanIds + '\'' + 
			",endDate = '" + endDate + '\'' + 
			",description = '" + description + '\'' + 
			",userTypes = '" + userTypes + '\'' + 
			",mediaId = '" + mediaId + '\'' + 
			",waiverPeriod = '" + waiverPeriod + '\'' + 
			",objectType = '" + objectType + '\'' + 
			",gracePeriodMinutes = '" + gracePeriodMinutes + '\'' + 
			",isInfiniteRenewal = '" + isInfiniteRenewal + '\'' + 
			",isCancellationBlocked = '" + isCancellationBlocked + '\'' + 
			",isRenewable = '" + isRenewable + '\'' + 
			",price = '" + price + '\'' + 
			",couponsGroups = '" + couponsGroups + '\'' + 
			",id = '" + id + '\'' + 
			",productCodes = '" + productCodes + '\'' + 
			",renewalsNumber = '" + renewalsNumber + '\'' + 
			",isWaiverEnabled = '" + isWaiverEnabled + '\'' + 
			",householdLimitationsId = '" + householdLimitationsId + '\'' + 
			",externalId = '" + externalId + '\'' + 
			",fileTypes = '" + fileTypes + '\'' + 
			",viewLifeCycle = '" + viewLifeCycle + '\'' + 
			",maxViewsNumber = '" + maxViewsNumber + '\'' + 
			",premiumServices = '" + premiumServices + '\'' + 
			",dependencyType = '" + dependencyType + '\'' + 
			",channels = '" + channels + '\'' + 
			",name = '" + name + '\'' + 
			",prorityInOrder = '" + prorityInOrder + '\'' + 
			",startDate = '" + startDate + '\'' + 
			"}";
		}
}