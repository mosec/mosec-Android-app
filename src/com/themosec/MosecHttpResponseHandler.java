package com.themosec;

public class MosecHttpResponseHandler {
	public void onStart() { }
	
	public void onSuccess(String response) { }
	
	public void onFailure(Throwable throwable) { }
	public void onFailure(Throwable throwable, String response) { }
	
	public void onFinish() { }
}
