package com.setradar;

import java.util.List;

import org.json.JSONArray;

import android.content.ContentResolver;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronizeContactsTask extends AsyncTask<ContentResolver, Void, Void> {
	@Override
	protected Void doInBackground(ContentResolver... contentResolvers) {
		ContentResolver contentResolver = contentResolvers[0];
		
		List<Contact> contacts = Contact.getAll(contentResolver, true);
		
		JSONArray contactsJson = Contact.toJsonFromContacts(contacts);
		
		RequestParams parameters = new RequestParams();
		
		String contactsJsonString = contactsJson.toString();
		
		parameters.put(DataManager.DATA_TYPE_PARAMETER_NAME, Contact.TYPE);
		parameters.put(DataManager.DATA_PARAMETER_NAME, contactsJsonString);
		
		RadarClient.post(DataManager.SYNCHRONIZE_ROUTE, parameters, new AsyncHttpResponseHandler() {
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
