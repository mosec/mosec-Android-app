package com.themosec;

import org.json.JSONArray;
import org.json.JSONObject;

public class MosecJsonResponseHandler extends MosecHttpResponseHandler {
	public void onSuccess(JSONObject objectJson) { }
	public void onSuccess(JSONArray objectsJson) { }
	
	public void onFailure(Throwable throwable, JSONObject errorJson) {}
    public void onFailure(Throwable throwable, JSONArray errorsJson) {}
}
