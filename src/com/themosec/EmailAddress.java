package com.themosec;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

public class EmailAddress implements Parcelable {
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public EmailAddress createFromParcel(Parcel parcel) {
	            	EmailAddress emailAddress = new EmailAddress(parcel);
	            	
	                return emailAddress;
	            }
	 
	            public EmailAddress[] newArray(int size) {
	                return new EmailAddress[size];
	            }
	        };
	
	private String mEmailAddress;
	
	public EmailAddress() {
		this((String)null);
	}
	
	public EmailAddress(String emailAddress) {
		mEmailAddress = emailAddress;
	}
	
	public EmailAddress(Parcel parcel) {
		readFromParcel(parcel);
	}
	
	public String getEmailAddress() {
		return mEmailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		mEmailAddress = emailAddress;
	}
	
	private void readFromParcel(Parcel parcel) {
		mEmailAddress = parcel.readString();
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mEmailAddress);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static List<EmailAddress> getAllForContact(ContentResolver contentResolver, long deviceContactId) {
		List<EmailAddress> emailAddresses = new ArrayList<EmailAddress>();
		
		Cursor emailAddressesCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " =" + deviceContactId, null, null); 
	    
		while (emailAddressesCursor.moveToNext()) {
	        String emailAddressString = emailAddressesCursor.getString(emailAddressesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	        
	        EmailAddress emailAddress = new EmailAddress();
	        
	        emailAddress.setEmailAddress(emailAddressString);
	        
	        emailAddresses.add(emailAddress);
	    }
		
		emailAddressesCursor.close();
		
		return emailAddresses;
	}
	
	public static String toStringFromEmailAddresses(List<EmailAddress> emailAddresses) {
		String emailAddressesString = "";
		
		for(int i = 0; i < emailAddresses.size(); i++) {
			EmailAddress emailAddress = emailAddresses.get(i);
			
			if(emailAddressesString.equals("")) {
				emailAddressesString += emailAddress.getEmailAddress();
			} else {
				emailAddressesString = emailAddressesString + "," + emailAddress.getEmailAddress();
			}
		}
		
		return emailAddressesString;
	}
}
