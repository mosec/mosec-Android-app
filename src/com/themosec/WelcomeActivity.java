package com.themosec;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity implements View.OnClickListener {
	private Context mContext;

    private BroadcastReceiver mInBroadcastReceiver;
    private IntentFilter mInIntentFilter;
    
	private Button mGoToSignInButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
		
		mInBroadcastReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            finish();
	        }
	    };
		
		mInIntentFilter = new IntentFilter(HomeActivity.IN_ACTION);

        registerReceiver(mInBroadcastReceiver, mInIntentFilter);
		
		setContentView(R.layout.welcome);
		
		mGoToSignInButton = (Button)findViewById(R.id.goToSignInButton);
		
		mGoToSignInButton.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(mInBroadcastReceiver);
	}

	@Override
	public void onClick(View view) {
		if(view == mGoToSignInButton) {
			Intent goToSignInIntent = new Intent(mContext, SignInActivity.class);
			
			startActivity(goToSignInIntent);
		}
	}
}
