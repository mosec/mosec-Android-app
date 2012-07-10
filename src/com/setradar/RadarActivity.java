package com.setradar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RadarActivity extends Activity {
	private Context mContext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = (Context)this;
        
        if(User.getCurrentUser() != null) {
        	Intent homeIntent = new Intent(mContext, HomeActivity.class);
        	
        	// Ensure user is brought back to application with a fresh start in terms of backwards navigation
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
        	startActivity(homeIntent);
        } else {
        	Intent welcomeIntent = new Intent(mContext, WelcomeActivity.class);
        	
        	// Ensure user is brought back to application with a fresh start in terms of backwards navigation
            welcomeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	
        	startActivity(welcomeIntent);
        }
    }
}