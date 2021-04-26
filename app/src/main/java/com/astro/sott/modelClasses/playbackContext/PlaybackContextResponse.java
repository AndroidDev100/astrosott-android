package com.astro.sott.modelClasses.playbackContext;

import com.google.gson.annotations.SerializedName;

public class PlaybackContextResponse{

	@SerializedName("executionTime")
	private double executionTime;

	@SerializedName("result")
	private Result result;

	public double getExecutionTime(){
		return executionTime;
	}

	public Result getResult(){
		return result;
	}
}