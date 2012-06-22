package com.mosecapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

public class PhoneNumber implements Parcelable {
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public PhoneNumber createFromParcel(Parcel parcel) {
	            	PhoneNumber phoneNumber = new PhoneNumber(parcel);
	            	
	                return phoneNumber;
	            }
	 
	            public PhoneNumber[] newArray(int size) {
	                return new PhoneNumber[size];
	            }
	        };
	
	private String mPhoneNumber;
	
	public PhoneNumber() {
		this((String)null);
	}
	
	public PhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	public PhoneNumber(Parcel parcel) {
		readFromParcel(parcel);
	}
	
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	private void readFromParcel(Parcel parcel) {
		mPhoneNumber = parcel.readString();
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mPhoneNumber);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static List<PhoneNumber> getAllForContact(ContentResolver contentResolver, long deviceContactId) {
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
		
		Cursor phoneNumbersCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + deviceContactId, null, null);
        
        while (phoneNumbersCursor.moveToNext()) {
        	String phoneNumberString = phoneNumbersCursor.getString(phoneNumbersCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        	
        	PhoneNumber phoneNumber = new PhoneNumber();
        	
        	phoneNumber.setPhoneNumber(phoneNumberString);
        	
        	phoneNumbers.add(phoneNumber);
        }
        
        phoneNumbersCursor.close();
        
        return phoneNumbers;
	}
	
	public static String toStringFromPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		String phoneNumbersString = "";
		
		for(int i = 0; i < phoneNumbers.size(); i++) {
			PhoneNumber phoneNumber = phoneNumbers.get(i);
			
			if(phoneNumbersString.equals("")) {
				phoneNumbersString += phoneNumber.getPhoneNumber();
			} else {
				phoneNumbersString = phoneNumbersString + "," + phoneNumber.getPhoneNumber();
			}
		}
		
		return phoneNumbersString;
	}
}
