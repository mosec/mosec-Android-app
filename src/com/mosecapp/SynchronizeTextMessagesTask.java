package com.mosecapp;

import java.util.List;

import org.json.JSONArray;

import android.content.ContentResolver;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronizeTextMessagesTask extends AsyncTask<ContentResolver, Void, Void> {
	@Override
	protected Void doInBackground(ContentResolver... contentResolvers) {
		ContentResolver contentResolver = contentResolvers[0];
		
		List<TextMessage> textMessages = TextMessage.getAll(contentResolver);
		
		JSONArray textMessagesJson = TextMessage.toJsonFromTextMessages(textMessages);
		
		RequestParams parameters = new RequestParams();
		
		String textMessagesJsonString = textMessagesJson.toString();

		parameters.put(DataManager.DATA_TYPE_PARAMETER_NAME, TextMessage.TYPE);
		parameters.put(DataManager.DATA_PARAMETER_NAME, textMessagesJsonString);
		
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
