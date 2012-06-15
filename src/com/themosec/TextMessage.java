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

public class TextMessage {
	public static final Uri TEXT_MESSAGES_URI = Uri.parse("content://sms");
	
	public static final String TYPE = "text_message";
	
	// From Telephony.TextBasedtextMessageColumns
	private static final int sAllMessageType = 0;
	private static final int sInboxMessageType = 1;
	private static final int sSentMessageType = 2;
	private static final int sDraftMessageType = 3;
	private static final int sOutboxMessageType = 4;
	private static final int sFailedMessageType = 5;
	private static final int sQueuedMessageType = 6;
	
	private static final String sAllType = "all";
	private static final String sInboxType = "inbox";
	private static final String sSentType = "sent";
	private static final String sDraftType = "draft";
	private static final String sOutboxType = "outbox";
	private static final String sFailedType = "failed";
	private static final String sQueuedType = "queued";
	
	private static final String sUidJsonKey = "uid";
	private static final String sTextMessageTypeJsonKey = "text_message_type";
	private static final String sThreadIdJsonKey = "thread_id";
	private static final String sPhoneNumberJsonKey = "phone_number";
	private static final String sBodyJsonKey = "body";
	private static final String sTimeJsonKey = "time";
	
	private long mUid;
	private String mTextMessageType;
	private long mThreadId;
	private String mPhoneNumber;
	private String mBody;
	private long mTime;
	
	public TextMessage() {
		this(-1, null, -1, null, null, -1);
	}
	
	public TextMessage(long uid) {
		this(uid, null, -1, null, null, -1);
	}
	
	public TextMessage(long uid, String textMessageType) {
		this(uid, textMessageType, -1, null, null, -1);
	}
	
	public TextMessage(long uid, String textMessageType, long threadId) {
		this(uid, textMessageType, threadId, null, null, -1);
	}
	
	public TextMessage(long uid, String textMessageType, long threadId, String phoneNumber) {
		this(uid, textMessageType, threadId, phoneNumber, null, -1);
	}
	
	public TextMessage(long uid, String textMessageType, long threadId, String phoneNumber, String body) {
		this(uid, textMessageType, threadId, phoneNumber, body, -1);
	}
	
	public TextMessage(long uid, String textMessageType, long threadId, String phoneNumber, String body, long time) {
		mUid = uid;
		mTextMessageType = textMessageType;
		mThreadId = threadId;
		mPhoneNumber = phoneNumber;
		mBody = body;
		mTime = time / 1000;
	}
	
	public long getUid() {
		return mUid;
	}
	
	public void setUid(long uid) {
		mUid = uid;
	}
	
	public String getTextMessageType() {
		return mTextMessageType;
	}
	
	public void setTextMessageType(int textMessageType) {
		switch(textMessageType) {
		case sAllMessageType:
			mTextMessageType = sAllType;
			
			break;
		case sInboxMessageType:
			mTextMessageType = sInboxType;
			
			break;
		case sSentMessageType:
			mTextMessageType = sSentType;
			
			break;
		case sDraftMessageType:
			mTextMessageType = sDraftType;
			
			break;
		case sOutboxMessageType:
			mTextMessageType = sOutboxType;
			
			break;
		case sFailedMessageType:
			mTextMessageType = sFailedType;
			
			break;
		case sQueuedMessageType:
			mTextMessageType = sQueuedType;
			
			break;
		}
	}
	
	public long getThreadId() {
		return mThreadId;
	}
	
	public void setThreadId(long threadId) {
		mThreadId = threadId;
	}
	
	public String getPhoneNumber() {
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		mPhoneNumber = phoneNumber;
	}
	
	public String getBody() {
		return mBody;
	}
	
	public void setBody(String body) {
		mBody = body;
	}
	
	public long getTime() {
		return mTime;
	}
	
	public void setTime(long time) {
		mTime = time / 1000;
	}
	
	public JSONObject toJson() {
		JSONObject textMessageJson = new JSONObject();
		
		try {
			textMessageJson.put(sUidJsonKey, mUid);
			textMessageJson.put(sTextMessageTypeJsonKey, mTextMessageType);
			textMessageJson.put(sThreadIdJsonKey, mThreadId);
			textMessageJson.put(sPhoneNumberJsonKey, mPhoneNumber);
			textMessageJson.put(sBodyJsonKey, mBody);
			textMessageJson.put(sTimeJsonKey, mTime);
		} catch(JSONException exception) {
			
		}
		
		return textMessageJson;
	}
	
	public static List<TextMessage> getAll(ContentResolver contentResolver) {
		List<TextMessage> textMessages = new ArrayList<TextMessage>();
		
		Cursor textMessagesCursor = contentResolver.query(TEXT_MESSAGES_URI, new String[] { "_id", "type", "thread_id", "address", "body", "date" }, null, null, null);

        while(textMessagesCursor.moveToNext()) {
	        long uid = textMessagesCursor.getLong(0);
			int textMessageType = textMessagesCursor.getInt(1);
			long threadId = textMessagesCursor.getLong(2);
			String phoneNumber = textMessagesCursor.getString(3);
			String body = textMessagesCursor.getString(4);
			long time = textMessagesCursor.getLong(5);
			
			TextMessage textMessage = new TextMessage();
			
			textMessage.setUid(uid);
			textMessage.setTextMessageType(textMessageType);
			textMessage.setThreadId(threadId);
			textMessage.setPhoneNumber(phoneNumber);
			textMessage.setBody(body);
			textMessage.setTime(time);
			
			textMessages.add(textMessage);
        }
        
        textMessagesCursor.close();
        
        return textMessages;
	}
	
	public static JSONArray toJsonFromTextMessages(List<TextMessage> textMessages) {
		Iterator<TextMessage> textMessagesIterator = textMessages.iterator();
		
		JSONArray textMessagesJson = new JSONArray();
		
		while(textMessagesIterator.hasNext()) {
    		TextMessage textMessage = textMessagesIterator.next();
			
			JSONObject textMessageJson = textMessage.toJson();
			
			textMessagesJson.put(textMessageJson);
		}
		
		return textMessagesJson;
	}
}
