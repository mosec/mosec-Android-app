package com.setradar;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class RadarContentObserver extends ContentObserver {
	private Uri mUri = null;
	
	public RadarContentObserver(Uri uri, Handler handler) {
		super(handler);
		
		mUri = uri;
	}
	
	@Override
	public boolean deliverSelfNotifications() {
		return super.deliverSelfNotifications();
	}
	
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		
		DataManager.onDataChanged(mUri);
	}
}
