package com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel;

public class DetailListItem{
	private String refAccount;
	private String switchStatus;
	private String lob;

	public void setRefAccount(String refAccount){
		this.refAccount = refAccount;
	}

	public String getRefAccount(){
		return refAccount;
	}

	public void setSwitchStatus(String switchStatus){
		this.switchStatus = switchStatus;
	}

	public String getSwitchStatus(){
		return switchStatus;
	}

	public void setLob(String lob){
		this.lob = lob;
	}

	public String getLob(){
		return lob;
	}

	@Override
 	public String toString(){
		return 
			"DetailListItem{" + 
			"refAccount = '" + refAccount + '\'' + 
			",switchStatus = '" + switchStatus + '\'' +
			",lob = '" + lob + '\'' +
			"}";
		}
}
