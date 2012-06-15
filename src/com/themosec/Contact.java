package com.themosec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

public class Contact implements Parcelable {
	public static final String KEY = "contact";
	
	public static final String TYPE = "contact";
	public static final String UID_KEY = "uid";
	
	public static final Uri CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI;
	
	private static final String sUidJsonKey = "uid";
	private static final String sFullNameJsonKey = "full_name";
	private static final String sPhoneNumbersJsonKey = "phone_numbers";
	private static final String sEmailAddressesJsonKey = "email_addresses";
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR =
	    	new Parcelable.Creator() {
	            public Contact createFromParcel(Parcel parcel) {
	            	Contact contact = new Contact(parcel);
	            	
	                return contact;
	            }
	 
	            public Contact[] newArray(int size) {
	                return new Contact[size];
	            }
	        };
	
	private long mUid;
	private String mFullName;
	private List<PhoneNumber> mPhoneNumbers;
	private List<EmailAddress> mEmailAddresses;
	
	public Contact() {
		this(-1, null);
	}
	
	public Contact(long uid) {
		this(uid, null);
	}
	
	public Contact(long uid, String fullName) {
		mUid = uid;
		mFullName = fullName;
		mPhoneNumbers = new ArrayList<PhoneNumber>();
		mEmailAddresses = new ArrayList<EmailAddress>();
	}
	
	public Contact(Parcel parcel) {
		mPhoneNumbers = new ArrayList<PhoneNumber>();
		mEmailAddresses = new ArrayList<EmailAddress>();
		
		readFromParcel(parcel);
	}
	
	public long getUid() {
		return mUid;
	}
	
	public void setUid(long uid) {
		mUid = uid;
	}
	
	public String getFullName() {
		return mFullName;
	}
	
	public void setFullName(String fullName) {
		mFullName = fullName;
	}
	
	public List<PhoneNumber> getPhoneNumbers() {
		return mPhoneNumbers;
	}
	
	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		mPhoneNumbers = phoneNumbers;
	}
	
	public List<EmailAddress> getEmailAddresses() {
		return mEmailAddresses;
	}
	
	public void setEmailAddresses(List<EmailAddress> emailAddresses) {
		mEmailAddresses = emailAddresses;
	}
	
	@SuppressWarnings("unchecked")
	private void readFromParcel(Parcel parcel) {
		mUid = parcel.readLong();
		mFullName = parcel.readString();
		
		parcel.readTypedList(mPhoneNumbers, PhoneNumber.CREATOR);
		parcel.readTypedList(mEmailAddresses, EmailAddress.CREATOR);
	}
	
	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeLong(mUid);
		parcel.writeString(mFullName);
		
		parcel.writeTypedList(mPhoneNumbers);
		parcel.writeTypedList(mEmailAddresses);
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public JSONObject toJson() {
		JSONObject contactJson = new JSONObject();
		
		try {
			contactJson.put(sUidJsonKey, mUid);
			contactJson.put(sFullNameJsonKey, mFullName);
			
			String phoneNumbersString = PhoneNumber.toStringFromPhoneNumbers(mPhoneNumbers);
			
			contactJson.put(sPhoneNumbersJsonKey, phoneNumbersString);
			
			String emailAddressesString = EmailAddress.toStringFromEmailAddresses(mEmailAddresses);
			
			contactJson.put(sEmailAddressesJsonKey, emailAddressesString);
		} catch(JSONException exception) {
			
		}
		
		return contactJson;
	}
	
	public static List<Contact> getAll(ContentResolver contentResolver, boolean justVisible) {		
		List<Contact> contacts = new ArrayList<Contact>();

		Cursor contactsCursor = null;
		
		if(justVisible) {
			contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.IN_VISIBLE_GROUP }, ContactsContract.Contacts.IN_VISIBLE_GROUP + " = 1", null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		} else {
			contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME }, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		}
		
		while(contactsCursor.moveToNext()) {
			long uid = contactsCursor.getLong(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
			String fullName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			
			List<PhoneNumber> phoneNumbers = PhoneNumber.getAllForContact(contentResolver, uid);
			List<EmailAddress> emailAddresses = EmailAddress.getAllForContact(contentResolver, uid);
			
			Contact contact = new Contact();
			
			contact.setUid(uid);
			contact.setFullName(fullName);
			contact.setPhoneNumbers(phoneNumbers);
			contact.setEmailAddresses(emailAddresses);
			
			contacts.add(contact);
		}

		contactsCursor.close();
		
		return contacts;
	}
	
	public static JSONArray toJsonFromContacts(List<Contact> contacts) {
		Iterator<Contact> contactsIterator = contacts.iterator();
		
		JSONArray contactsJson = new JSONArray();
		
		while(contactsIterator.hasNext()) {
    		Contact contact = contactsIterator.next();
			
			JSONObject contactJson = contact.toJson();
			
			contactsJson.put(contactJson);
		}
		
		return contactsJson;
	}
}
