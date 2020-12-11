package com.astro.sott.modelClasses.dmsResponse;

public class ParentalRules{
	private String all;
	private String jsonMember7;
	private String jsonMember18;
	private String jsonMember16;
	private String jsonMember13;

	public void setAll(String all){
		this.all = all;
	}

	public String getAll(){
		return all;
	}

	public void setJsonMember7(String jsonMember7){
		this.jsonMember7 = jsonMember7;
	}

	public String getJsonMember7(){
		return jsonMember7;
	}

	public void setJsonMember18(String jsonMember18){
		this.jsonMember18 = jsonMember18;
	}

	public String getJsonMember18(){
		return jsonMember18;
	}

	public void setJsonMember16(String jsonMember16){
		this.jsonMember16 = jsonMember16;
	}

	public String getJsonMember16(){
		return jsonMember16;
	}

	public void setJsonMember13(String jsonMember13){
		this.jsonMember13 = jsonMember13;
	}

	public String getJsonMember13(){
		return jsonMember13;
	}

	@Override
 	public String toString(){
		return 
			"ParentalRules{" + 
			"all = '" + all + '\'' + 
			",7+ = '" + jsonMember7 + '\'' + 
			",18+ = '" + jsonMember18 + '\'' + 
			",16+ = '" + jsonMember16 + '\'' + 
			",13+ = '" + jsonMember13 + '\'' + 
			"}";
		}
}
