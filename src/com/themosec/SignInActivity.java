package com.themosec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends Activity implements View.OnKeyListener, View.OnClickListener {
	private Context mContext;
	private Resources mResources;
	private InputMethodManager mInputMethodManager;
	
	private EditText mEmailAddressEditText;
	private EditText mPasswordEditText;
	private Button mSignInButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = (Context)this;
		mResources = mContext.getResources();
		mInputMethodManager = (InputMethodManager)mContext.getSystemService(INPUT_METHOD_SERVICE);
		
		setContentView(R.layout.sign_in);
		
		mEmailAddressEditText = (EditText)findViewById(R.id.signInEmailAddressEditText);
		mPasswordEditText = (EditText)findViewById(R.id.signInPasswordEditText);
		mSignInButton = (Button)findViewById(R.id.signInButton);

		mPasswordEditText.setOnKeyListener(this);
		
		mSignInButton.setOnClickListener(this);
		
		// Set focus on email address edit text and pop up keyboard automatically if there is no physical keyboard already
		mEmailAddressEditText.postDelayed(new Runnable() {
			@Override
			public void run() {
				mInputMethodManager.showSoftInput(mEmailAddressEditText, InputMethodManager.SHOW_IMPLICIT);
			}
		}, 75);
	}
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		if(view == mPasswordEditText) {
	        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
	        	mSignInButton.performClick();
	          
	        	return true;
	        }
		}
        
        return false;
	}
	
	@Override
	public void onClick(View view) {
		if(view == mSignInButton) {
			final ProgressDialog progressDialog = ProgressDialog.show(SignInActivity.this, mResources.getString(R.string.progress_dialog_title), mResources.getString(R.string.progress_dialog_message), true);
			
			String emailAddress = mEmailAddressEditText.getText().toString();
			String password = mPasswordEditText.getText().toString();
			
			User user = new User();
			
			user.setEmailAddress(emailAddress);
			user.setPassword(password);
			
			user.signInInBackground(mContext, new MosecJsonResponseHandler() {
				@Override
				public void onSuccess(JSONObject userJson) {
					Intent homeIntent = new Intent(mContext, HomeActivity.class);
					
					startActivity(homeIntent);
					
					Intent broadcastIntent = new Intent();
					
					broadcastIntent.setAction(HomeActivity.IN_ACTION);
					
					sendBroadcast(broadcastIntent);
					
					finish();

					progressDialog.dismiss();
				}
				
				@Override
				public void onFailure(Throwable throwable, JSONArray errorsJson) {
					String[] errors = new String[errorsJson.length()];
					
					for(int i = 0; i < errorsJson.length(); i++) {
						try {
							String error = errorsJson.getString(i);
							
							errors[i] = error;
						} catch (JSONException exception) {
							
						}
					}
					
					Bundle bundle = new Bundle();
					
					bundle.putStringArray(MosecApplication.ERRORS_KEY, errors);
					
					showDialog(MosecApplication.ERROR_DIALOG, bundle);
					
					mPasswordEditText.setText("");
					
					progressDialog.dismiss();
				}
			});
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
	    switch (id) {
	    case MosecApplication.ERROR_DIALOG:
	    	AlertDialog alertDialog = (AlertDialog)dialog;

			String[] errors = bundle.getStringArray(MosecApplication.ERRORS_KEY);
			
			String message = MosecApplication.toStringFromErrorsStringArray(errors);
			
	    	alertDialog.setMessage(message);
	        
	    	break;
	    }
	}
	
	@Override
	public Dialog onCreateDialog(int id, Bundle bundle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		switch(id) {
		case MosecApplication.ERROR_DIALOG:
			// Setting message to an empty string is a hack because of a bug in Google's code (http://code.google.com/p/android/issues/detail?id=6489)
	    	builder.setMessage("")
	    		   .setCancelable(true)
	    		   .setPositiveButton(mResources.getString(R.string.positive_alert_dialog_button_text), null);
	    	
	    	break;
		}
		
    	AlertDialog alertDialog = builder.create();
    	
    	return alertDialog;
	}
}
