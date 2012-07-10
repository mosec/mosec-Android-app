package com.setradar;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class SynchronizationService extends Service {
	private static boolean sStarted = false;
	
	private Context mContext;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		mContext = this;
		
		DataManager.initialize(mContext);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		sStarted = true;
		
		DataManager.synchronizeAllData();
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		sStarted = false;
		
	   DataManager.destroy();
	}
	
	public static boolean isStarted() {
		return sStarted;
	}
}
