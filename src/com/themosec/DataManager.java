package com.themosec;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

public class DataManager {
	public static final String SYNCHRONIZE_ROUTE = "/data/synchronize.json";

	public static final String CONTACTABLE_TYPE_PARAMETER_NAME = "contactable_type";
	public static final String DATA_TYPE_PARAMETER_NAME = "data[type]";
	public static final String DATA_PARAMETER_NAME = "data[data]";
	
	public static final String CONTACTABLE_TYPE = "phone";
	
	public static boolean sContactsUriValid = false;
	public static boolean sCallsUriValid = false;
	public static boolean sTextMessagesUriValid = false;
	public static boolean sCalendarEventsUriValid = false;
	
	private static final Uri[] URIS_TO_BE_TESTED = { Contact.CONTACTS_URI, Call.CALLS_URI, TextMessage.TEXT_MESSAGES_URI, CalendarEvent.CALENDARS_URI };
	
	private static Context sContext;
    private static ContentResolver sContentResolver;
    
    private static List<Uri> sUris;
	private static List<MosecContentObserver> sContentObservers;
 
    public static void initialize(Context context) {
		sContext = context;
		sContentResolver = sContext.getContentResolver();
		
		sUris = new ArrayList<Uri>();
		sContentObservers = new ArrayList<MosecContentObserver>();
		
		for(int i = 0; i < URIS_TO_BE_TESTED.length; i++) {
			Cursor uriCursor = null;

			Uri uriToBeTested = URIS_TO_BE_TESTED[i];
			
			try {
				uriCursor = sContentResolver.query(uriToBeTested, null, null, null, null);

				uriCursor.close();
				
				if(uriToBeTested == Contact.CONTACTS_URI) {
					sContactsUriValid = true;
				} else if(uriToBeTested == Call.CALLS_URI) {
					sCallsUriValid = true;
				} else if(uriToBeTested == TextMessage.TEXT_MESSAGES_URI) {
					sTextMessagesUriValid = true;
				} else if(uriToBeTested == CalendarEvent.CALENDARS_URI) {
					sCalendarEventsUriValid = true;
				}

				sUris.add(uriToBeTested);
			} catch(NullPointerException exception) {

			}    
		}
		
		for(int i = 0; i < sUris.size(); i++) {
			Handler handler = new Handler();
			
			Uri uri = sUris.get(i);
			
			MosecContentObserver mosecContentObserver = new MosecContentObserver(uri, handler);
			
			sContentObservers.add(mosecContentObserver);
		}
		
		for(int i = 0; i < sContentObservers.size(); i++) {
			MosecContentObserver mosecContentObserver = sContentObservers.get(i);
			
			sContentResolver.registerContentObserver(sUris.get(i), true, mosecContentObserver);
		}
    }
    
    public static void destroy() {
    	for(int i = 0; i < sContentObservers.size(); i++) {
			MosecContentObserver mosecContentObserver = sContentObservers.get(i);
			
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
