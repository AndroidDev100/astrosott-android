package com.astro.sott.activities.SelectAccount.SelectAccountModel;

import java.util.List;

public class Data{
	private List<DetailListItem> detailList;

	public void setDetailList(List<DetailListItem> detailList){
		this.detailList = detailList;
	}

	public List<DetailListItem> getDetailList(){
		return detailList;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"detailList = '" + detailList + '\'' + 
			"}";
		}
}