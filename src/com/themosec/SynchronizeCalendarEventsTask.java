package com.themosec;

import java.util.List;

import org.json.JSONArray;

import android.content.ContentResolver;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SynchronizeCalendarEventsTask extends AsyncTask<ContentResolver, Void, Void> {
	@Override
	protected Void doInBackground(ContentResolver... contentResolvers) {
		ContentResolver contentResolver = contentResolvers[0];

		List<CalendarEvent> calendarEvents = CalendarEvent.getAll(contentResolver);
		
		JSONArray calendarEventsJson = CalendarEvent.toJsonFromCalendarEvents(calendarEvents);
		
		RequestParams parameters = new RequestParams();
		
		String calendarEventsJsonString = calendarEventsJson.toString();
		
		parameters.put(DataManager.DATA_TYPE_PARAMETER_NAME, CalendarEvent.TYPE);
		parameters.put(DataManager.DATA_PARAMETER_NAME, calendarEventsJsonString);
		
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

