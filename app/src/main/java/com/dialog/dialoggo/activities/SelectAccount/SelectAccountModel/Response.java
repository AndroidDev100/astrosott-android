package com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel;

public class Response{
	private String apiStatusCode;
	private Data data;
	private String message;

	public void setApiStatusCode(String apiStatusCode){
		this.apiStatusCode = apiStatusCode;
	}

	public String getApiStatusCode(){
		return apiStatusCode;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"apiStatusCode = '" + apiStatusCode + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}
