package com.themosec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeActivity extends PreferenceActivity {
	public static final String IN_ACTION = "com.getmosec.actions.IN_ACTION";

	private Context mContext;
	
	private PreferenceCategory mDashboardPreferenceCategory;
	private EditTextPreference mCallsEditTextPreference;
	private EditTextPreference mTextMessagesEditTextPreference;
	private EditTextPreference mCalendarEventsEditTextPreference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = (Context)this;
		
		addPreferencesFromResource(R.xml.home);
		
		mDashboardPreferenceCategory = (PreferenceCategory)findPreference("dashboard_preference_category");
		mCallsEditTextPreference = (EditTextPreference)findPreference("calls_edit_text_preference");
		mTextMessagesEditTextPreference = (EditTextPreference)findPreference("text_messages_edit_text_preference");
		mCalendarEventsEditTextPreference = (EditTextPreference)findPreference("calendar_events_edit_text_preference");

		mDashboardPreferenceCategory.setTitle(mDashboardPreferenceCategory.getTitle().toString() + " for " + User.getCurrentUser().getEmailAddress());
		
		mCallsEditTextPreference.setSelectable(false);
		mTextMessagesEditTextPreference.setSelectable(false);
		mCalendarEventsEditTextPreference.setSelectable(false);
		
		if(!DataManager.sCallsUriValid) {
			mCallsEditTextPreference.setSummary("Calls cannot be indexed");
			mCallsEditTextPreference.setEnabled(false);
		}
		if(!DataManager.sTextMessagesUriValid) {
			mTextMessagesEditTextPreference.setSummary("Messages cannot be indexed");
			mTextMessagesEditTextPreference.setEnabled(false);
		}
		if(!DataManager.sCalendarEventsUriValid) {
			mCalendarEventsEditTextPreference.setSummary("Calendar events cannot be indexed");
			mCalendarEventsEditTextPreference.setEnabled(false);
		}
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
}
