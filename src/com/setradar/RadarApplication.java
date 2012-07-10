package com.setradar;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;

public class RadarApplication extends Application {
	public static final String ERRORS_KEY = "ERRORS";
	
	public static final int ERROR_DIALOG = 1;
	
	private Context mContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mContext = (Context)this;

        ActiveAndroid.initialize(mContext);
		EnvironmentHelper.setEnvironment(EnvironmentHelper.PRODUCTION);
		RadarClient.setup(mContext);
	}
	
	public static String toStringFromErrorsStringArray(String[] errors) {
		String message = "";
		
		if(errors.length == 1) {
			String error = errors[0];
			
			message = error;
		} else {
			for(int i = 0; i < errors.length; i++) {
				String error = "\u2022 " + errors[i];
				
				if(i < errors.length - 1) {
					error += "\n";
				}
				
				message += error;
			}
		}
		
		return message;
	}
}
