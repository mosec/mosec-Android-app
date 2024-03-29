package com.setradar;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class DataManager {
	public static final String SYNCHRONIZE_ROUTE = "/phone_data/synchronize.json";

	public static final String DATA_TYPE_PARAMETER_NAME = "phone_data[type]";
	public static final String DATA_PARAMETER_NAME = "phone_data[data]";
	
	public static final String LISTENING_ACTION = "com.setradar.actions.LISTENING_ACTION";
	
	public static final String LISTENING_TO_CONTACTS_URI_KEY = "LISTENING_TO_CONTACTS_URI";
	public static final String LISTENING_TO_CALLS_URI_KEY = "LISTENING_TO_CALLS_URI";
	public static final String LISTENING_TO_TEXT_MESSAGES_URI_KEY = "LISTENING_TO_TEXT_MESSAGES_URI";
	public static final String LISTENING_TO_CALENDAR_EVENTS_URI_KEY = "LISTENING_TO_CALENDAR_EVENTS_URI";
	
	public static boolean sListeningToContactsUri = false;
	public static boolean sListeningToCallsUri = false;
	public static boolean sListeningToTextMessagesUri = false;
	public static boolean sListeningToCalendarEventsUri = false;
	
	private static final Uri[] URIS_TO_BE_TESTED = { Contact.CONTACTS_URI, Call.CALLS_URI, TextMessage.TEXT_MESSAGES_URI, CalendarEvent.CALENDARS_URI };
	
	private static Context sContext;
    private static ContentResolver sContentResolver;
    
    private static List<Uri> sUris;
	private static List<RadarContentObserver> sContentObservers;
 
    public static void initialize(Context context) {
		sContext = context;
		sContentResolver = sContext.getContentResolver();
		
		sUris = new ArrayList<Uri>();
		sContentObservers = new ArrayList<RadarContentObserver>();
		
		for(int i = 0; i < URIS_TO_BE_TESTED.length; i++) {
			Cursor uriCursor = null;

			Uri uriToBeTested = URIS_TO_BE_TESTED[i];
			
			try {
				uriCursor = sContentResolver.query(uriToBeTested, null, null, null, null);

				uriCursor.close();

				sUris.add(uriToBeTested);
			} catch(NullPointerException exception) {

			}    
		}
		
		for(int i = 0; i < sUris.size(); i++) {
			Handler handler = new Handler();
			
			Uri uri = sUris.get(i);
			
			RadarContentObserver mosecContentObserver = new RadarContentObserver(uri, handler);
			
			sContentObservers.add(mosecContentObserver);
		}
		
		for(int i = 0; i < sContentObservers.size(); i++) {
			RadarContentObserver mosecContentObserver = sContentObservers.get(i);
			
			Uri uri = sUris.get(i);
			
			sContentResolver.registerContentObserver(uri, true, mosecContentObserver);
			
			if(uri == Contact.CONTACTS_URI) {
				sListeningToContactsUri = true;
			} else if(uri == Call.CALLS_URI) {
				sListeningToCallsUri = true;
			} else if(uri == TextMessage.TEXT_MESSAGES_URI) {
				sListeningToTextMessagesUri = true;
			} else if(uri == CalendarEvent.CALENDARS_URI) {
				sListeningToCalendarEventsUri = true;
			}
		}
		
		Intent broadcastIntent = new Intent();

		broadcastIntent.setAction(LISTENING_ACTION);

		broadcastIntent.putExtra(LISTENING_TO_CONTACTS_URI_KEY, sListeningToContactsUri);
		broadcastIntent.putExtra(LISTENING_TO_CALLS_URI_KEY, sListeningToCallsUri);
		broadcastIntent.putExtra(LISTENING_TO_TEXT_MESSAGES_URI_KEY, sListeningToTextMessagesUri);
		broadcastIntent.putExtra(LISTENING_TO_CALENDAR_EVENTS_URI_KEY, sListeningToCalendarEventsUri);

		sContext.sendBroadcast(broadcastIntent);
    }
    
    public static void destroy() {
    	for(int i = 0; i < sContentObservers.size(); i++) {
			RadarContentObserver mosecContentObserver = sContentObservers.get(i);
			
            sContentResolver.unregisterContentObserver(mosecContentObserver);
    	}
    }
    
    public static void synchronizeAllData() {
    	for(int i = 0; i < sUris.size(); i++) {
    		Uri uri = sUris.get(i);
    		
    		synchronizeData(uri);
    	}
    }
 
    private static void synchronizeData(Uri uri) {
    	if(uri == Contact.CONTACTS_URI) {
			SynchronizeContactsTask synchronizeContactsTask = new SynchronizeContactsTask();
			
			synchronizeContactsTask.execute(sContentResolver);
    	} else if(uri == Call.CALLS_URI) {
    		SynchronizeCallsTask synchronizeCallsTask = new SynchronizeCallsTask();
    		
    		synchronizeCallsTask.execute(sContentResolver);
    	} else if(uri == TextMessage.TEXT_MESSAGES_URI) {
    		SynchronizeTextMessagesTask synchronizeTextMessagesTask = new SynchronizeTextMessagesTask();
    		
    		synchronizeTextMessagesTask.execute(sContentResolver);
    	} else if(uri == CalendarEvent.CALENDARS_URI) {
    		SynchronizeCalendarEventsTask synchronizeCalendarEventsTask = new SynchronizeCalendarEventsTask();
    		
    		synchronizeCalendarEventsTask.execute(sContentResolver);
    	}
    }
 
    public static void onDataChanged(Uri uri) {
        synchronizeData(uri);
    }
}
