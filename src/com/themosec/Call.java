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
import android.provider.CallLog;

public class Call {
	public static final Uri CALLS_URI = CallLog.Calls.CONTENT_URI;
	
	public static final String TYPE = "call";
	
	private static final String sIncomingType = "incoming";
	private static final String sOutgoingType = "outgoing";
	private static final String sMissedType = "missed";

	private static final String sUidJsonKey = "uid";
	private static final String sCallTypeJsonKey = "call_type";
	private static final String sFullNameJsonKey = "full_name";
	private static final String sPhoneNumberJsonKey = "phone_number";
	private static final String sDurationJsonKey = "duration";
	private static final String sTimeJsonKey = "time";
	
	private long mUid;
	private String mCallType;
	private String mFullName;
	private String mPhoneNumber;
	private long mDuration;
	private long mTime;
	
	public Call() {
		this(-1, null, null, null, -1, -1);
	}
	
	public Call(long uid) {
		this(uid, null, null, null, -1, -1);
	}
	
	public Call(long uid, String callType) {
		this(uid, callType, null, null, -1, -1);
	}
	
	public Call(long uid, String callType, String fullName) {
		this(uid, callType, fullName, null, -1 -1);
	}
	
	public Call(long uid, String callType, String fullName, String phoneNumber) {
		this(uid, callType, fullName, phoneNumber, -1, -1);
	}
	
	public Call(long uid, String callType, String fullName, String phoneNumber, long duration) {
		this(uid, callType, fullName, phoneNumber, duration, -1);
	}
	
	public Call(long uid, String callType, String fullName, String phoneNumber, long duration, long time) {
		mUid = uid;
		mCallType = callType;
		mFullName = fullName;
		mPhoneNumber = phoneNumber;
		mDuration = duration;
		mTime = time / 1000;
	}
	
	public long getUid() {
		return mUid;
	}
	
	public void setUid(long uid) {
		mUid = uid;
	}
	
	public String getCallType() {
		return mCallType;
	}
	
	public void setCallType(int callType) {
		switch(callType) {
		case CallLog.Calls.INCOMING_TYPE:
			mCallType = sIncomingType;
			
			break;
		case CallLog.Calls.OUTGOING_TYPE:
			mCallType = sOutgoingType;
			
			break;
		case CallLog.Calls.MISSED_TYPE:
			mCallType = sMissedType;
			
			break;
		}
	}
	
	public String getFullName() {
		return mFullName;
	}
	
	public void setFullName(String fullName) {
		mFullName = fullName;
	}
	
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	public long getDuration() {
		return mDuration;
	}
	
	public void setDuration(long duration) {
		mDuration = duration;
	}
	
	public long getTime() {
		return mTime;
	}
	
	public void setTime(long time) {
		mTime = time / 1000;
	}
	
	public JSONObject toJson() {
		JSONObject callJson = new JSONObject();
		
		try {
			callJson.put(sUidJsonKey, mUid);
			callJson.put(sCallTypeJsonKey, mCallType);
			callJson.put(sFullNameJsonKey, mFullName);
			callJson.put(sPhoneNumberJsonKey, mPhoneNumber);
			callJson.put(sDurationJsonKey, mDuration);
			callJson.put(sTimeJsonKey, mTime);
		} catch(JSONException exception) {
			
		}
		
		return callJson;
	}
	
	public static List<Call> getAll(ContentResolver contentResolver) {	
		List<Call> calls = new ArrayList<Call>();
		
		Cursor callsCursor = contentResolver.query(CallLog.Calls.CONTENT_URI, new String[] { CallLog.Calls._ID, CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DURATION, CallLog.Calls.DATE }, null, null, null);
		
		while(callsCursor.moveToNext()) {
			long uid = callsCursor.getLong(callsCursor.getColumnIndex(CallLog.Calls._ID));
			int callType = Integer.parseInt(callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.TYPE)));
			String fullName = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
			String phoneNumber = callsCursor.getString(callsCursor.getColumnIndex(CallLog.Calls.NUMBER));
			long duration = callsCursor.getLong(callsCursor.getColumnIndex(CallLog.Calls.DURATION));
			long time = callsCursor.getLong(callsCursor.getColumnIndex(CallLog.Calls.DATE));
			
			Call call = new Call();
			
			call.setUid(uid);
			call.setCallType(callType);
			call.setFullName(fullName);
			call.setPhoneNumber(phoneNumber);
			call.setDuration(duration);
			call.setTime(time);
			
			calls.add(call);
		}
		
		callsCursor.close();
		
		return calls;
	}
	
	public static JSONArray toJsonFromCalls(List<Call> calls) {
		Iterator<Call> callsIterator = calls.iterator();
		
		JSONArray callsJson = new JSONArray();
		
		while(callsIterator.hasNext()) {
    		Call call = callsIterator.next();
			
			JSONObject callJson = call.toJson();
			
			callsJson.put(callJson);
		}
		
		return callsJson;
	}
}
