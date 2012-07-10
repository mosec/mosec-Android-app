package com.setradar;

import org.json.JSONArray;
import org.json.JSONObject;

public class RadarJsonResponseHandler extends RadarHttpResponseHandler {
	public void onSuccess(JSONObject objectJson) { }
	public void onSuccess(JSONArray objectsJson) { }
	
	public void onFailure(Throwable throwable, JSONObject errorJson) {}
    public void onFailure(Throwable throwable, JSONArray errorsJson) {}
}
