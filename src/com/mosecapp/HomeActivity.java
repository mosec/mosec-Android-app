package com.mosecapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeActivity extends PreferenceActivity {
	public static final String IN_ACTION = "com.mosecapp.actions.IN_ACTION";

	private Context mContext;
	private Resources mResources;
	
	private PreferenceCategory mDashboardPreferenceCategory;
	private EditTextPreference mContactsEditTextPreference;
	private EditTextPreference mCallsEditTextPreference;
	private EditTextPreference mTextMessagesEditTextPreference;
	private EditTextPreference mCalendarEventsEditTextPreference;

    private BroadcastReceiver mListeningBroadcastReceiver;
    private IntentFilter mListeningIntentFilter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = (Context)this;
		mResources = mContext.getResources();
		
		addPreferencesFromResource(R.xml.home);
		
		mDashboardPreferenceCategory = (PreferenceCategory)findPreference("dashboard_preference_category");
		mContactsEditTextPreference = (EditTextPreference)findPreference("contacts_edit_text_preference");
		mCallsEditTextPreference = (EditTextPreference)findPreference("calls_edit_text_preference");
		mTextMessagesEditTextPreference = (EditTextPreference)findPreference("text_messages_edit_text_preference");
		mCalendarEventsEditTextPreference = (EditTextPreference)findPreference("calendar_events_edit_text_preference");

		mDashboardPreferenceCategory.setTitle(mDashboardPreferenceCategory.getTitle().toString() + " for " + User.getCurrentUser().getEmailAddress());

		mContactsEditTextPreference.setSelectable(false);
		mCallsEditTextPreference.setSelectable(false);
		mTextMessagesEditTextPreference.setSelectable(false);
		mCalendarEventsEditTextPreference.setSelectable(false);
		
    	mContactsEditTextPreference.setEnabled(false);
    	mCallsEditTextPreference.setEnabled(false);
    	mTextMessagesEditTextPreference.setEnabled(false);
    	mCalendarEventsEditTextPreference.setEnabled(false);
    	
    	setListeningToContentPreferences(DataManager.sListeningToContactsUri, DataManager.sListeningToCallsUri, DataManager.sListeningToTextMessagesUri, DataManager.sListeningToCalendarEventsUri);
    	
		mListeningBroadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	        	boolean listeningToContactsUri = intent.getBooleanExtra(DataManager.LISTENING_TO_CONTACTS_URI_KEY, false);
	        	boolean listeningToCallsUri = intent.getBooleanExtra(DataManager.LISTENING_TO_CALLS_URI_KEY, false);
	        	boolean listeningToTextMessagesUri = intent.getBooleanExtra(DataManager.LISTENING_TO_TEXT_MESSAGES_URI_KEY, false);
	        	boolean listeningToCalendarEventsUri = intent.getBooleanExtra(DataManager.LISTENING_TO_CALENDAR_EVENTS_URI_KEY, false);
	        	
	        	setListeningToContentPreferences(listeningToContactsUri, listeningToCallsUri, listeningToTextMessagesUri, listeningToCalendarEventsUri);
	        }
	    };
		
		mListeningIntentFilter = new IntentFilter(DataManager.LISTENING_ACTION);

        registerReceiver(mListeningBroadcastReceiver, mListeningIntentFilter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
    	
		// To ensure the synchronization service is always on
		if(!SynchronizationService.isStarted()) {
			Intent synchronizationService = new Intent(mContext, SynchronizationService.class);
			
			startService(synchronizationService);	
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    
	    inflater.inflate(R.menu.home, menu);
	    
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.logoutOption:
			User.getCurrentUser().logout();
		
			Intent welcomeIntent = new Intent(mContext, WelcomeActivity.class);
		
			startActivity(welcomeIntent);
			
			Intent synchronizationIntent = new Intent(mContext, SynchronizationService.class);
			
			stopService(synchronizationIntent);
		
			finish();
			
			break;
	    }
		
		return true;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(mListeningBroadcastReceiver);
	}
	
	public void setListeningToContentPreferences(boolean listeningToContactsUri, boolean listeningToCallsUri, boolean listeningToTextMessagesUri, boolean listeningToCalendarEventsUri) {
		if(listeningToContactsUri) {
        	mContactsEditTextPreference.setSummary(mResources.getString(R.string.contacts_edit_text_preference_positive_summary));
        	mContactsEditTextPreference.setEnabled(true);
        }
        if(listeningToCallsUri) {
        	mCallsEditTextPreference.setSummary(mResources.getString(R.string.calls_edit_text_preference_positive_summary));
        	mCallsEditTextPreference.setEnabled(true);
        }
        if(listeningToTextMessagesUri) {
        	mTextMessagesEditTextPreference.setSummary(mResources.getString(R.string.text_messages_edit_text_preference_positive_summary));
        	mTextMessagesEditTextPreference.setEnabled(true);
        }
        if(listeningToCalendarEventsUri) {
        	mCalendarEventsEditTextPreference.setSummary(mResources.getString(R.string.calendar_events_edit_text_preference_positive_summary));
        	mCalendarEventsEditTextPreference.setEnabled(true);
        }
	}
}
