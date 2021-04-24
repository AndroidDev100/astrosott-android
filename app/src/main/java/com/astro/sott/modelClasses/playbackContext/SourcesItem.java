package com.astro.sott.modelClasses.playbackContext;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SourcesItem{

	@SerializedName("altUrl")
	private String altUrl;

	@SerializedName("businessModuleDetails")
	private BusinessModuleDetails businessModuleDetails;

	@SerializedName("format")
	private String format;

	@SerializedName("externalId")
	private String externalId;

	@SerializedName("opl")
	private String opl;

	@SerializedName("isTokenized")
	private boolean isTokenized;

	@SerializedName("type")
	private String type;

	@SerializedName("url")
	private String url;

	@SerializedName("objectType")
	private String objectType;

	@SerializedName("duration")
	private int duration;

	@SerializedName("fileSize")
	private int fileSize;

	@SerializedName("assetId")
	private int assetId;

	@SerializedName("id")
	private int id;

	@SerializedName("protocols")
	private String protocols;

	@SerializedName("drm")
	private List<DrmItem> drm;

	public String getAltUrl(){
		return altUrl;
	}

	public BusinessModuleDetails getBusinessModuleDetails(){
		return businessModuleDetails;
	}

	public String getFormat(){
		return format;
	}

	public String getExternalId(){
		return externalId;
	}

	public String getOpl(){
		return opl;
	}

	public boolean isIsTokenized(){
		return isTokenized;
	}

	public String getType(){
		return type;
	}

	public String getUrl(){
		return url;
	}

	public String getObjectType(){
		return objectType;
	}

	public int getDuration(){
		return duration;
	}

	public int getFileSize(){
		return fileSize;
	}

	public int getAssetId(){
		return assetId;
	}

	public int getId(){
		return id;
	}

	public String getProtocols(){
		return protocols;
	}

	public List<DrmItem> getDrm(){
		return drm;
	}
}