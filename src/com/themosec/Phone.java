package com.themosec;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class Phone {
	public static final String PROVIDER_PARAMETER_NAME = "source[provider]";
	public static final String UID_PARAMETER_NAME = "source[uid]";

	public static final String PROVIDER ="android";
	
	private static Phone sPhone = null;
	
	private String mUid = null;
	
	private Phone(String uid) {
		mUid = uid;
		
		sPhone = this;
	}
	
	public String getUid() {
		return mUid;
	}
	
	public void setUid(String uid) {
		mUid = uid;
	}
	
	public static Phone getInstance(Context context) {
		if(sPhone != null) {
			return sPhone;
		} else {
			ContentResolver contentResolver = context.getContentResolver();
			
			String uid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
			
			return new Phone(uid);
		}
	}
}
