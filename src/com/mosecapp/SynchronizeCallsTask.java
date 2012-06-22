package com.mosecapp;

import java.util.List;

import org.json.JSONArray;

import android.content.ContentResolver;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronizeCallsTask extends AsyncTask<ContentResolver, Void, Void> {
	@Override
	protected Void doInBackground(ContentResolver... contentResolvers) {
		ContentResolver contentResolver = contentResolvers[0];
		
		List<Call> calls = Call.getAll(contentResolver);
		
		JSONArray callsJson = Call.toJsonFromCalls(calls);
		
		RequestParams parameters = new RequestParams();
		
		String callsJsonString = callsJson.toString();

		parameters.put(DataManager.DATA_TYPE_PARAMETER_NAME, Call.TYPE);
		parameters.put(DataManager.DATA_PARAMETER_NAME, callsJsonString);
		
		MosecClient.post(DataManager.SYNCHRONIZE_ROUTE, parameters, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				
			}
			
			@Override
			public void onFailure(Throwable exception) {
				
			}
		});
		
		return null;
	}
}
