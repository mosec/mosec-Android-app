package com.mosecapp;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class MosecContentObserver extends ContentObserver {
	private Uri mUri = null;
	
	public MosecContentObserver(Uri uri, Handler handler) {
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
