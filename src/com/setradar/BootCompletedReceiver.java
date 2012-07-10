package com.setradar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(User.getCurrentUser() != null) {
			Intent synchronizationServiceIntent = new Intent(context, SynchronizationService.class);
			
			context.startService(synchronizationServiceIntent);
		}
	}
}
