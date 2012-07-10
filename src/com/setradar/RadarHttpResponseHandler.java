package com.setradar;

public class RadarHttpResponseHandler {
	public void onStart() { }
	
	public void onSuccess(String response) { }
	
	public void onFailure(Throwable throwable) { }
	public void onFailure(Throwable throwable, String response) { }
	
	public void onFinish() { }
}
