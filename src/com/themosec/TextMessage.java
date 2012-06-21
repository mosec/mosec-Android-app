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
	private int mTextMessageType;
	private long mThreadId;
	private String mPhoneNumber;
	private String mBody;
	private long mTime;
	
	public TextMessage() {
		mUid = -1;
		mTextMessageType = -1;
		mThreadId = -1;
		mPhoneNumber = null;
		mBody = null;
		mTime = -1;
	}
	
	public long getUid() {
		return mUid;
	}
	
	public void setUid(long uid) {
		mUid = uid;
	}
	
	public int getTextMessageType() {
		return mTextMessageType;
	}
	
	public void setTextMessageType(int textMessageType) {
		mTextMessageType = textMessageType;
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
			// Normalizing for backend
			textMessageJson.put(sUidJsonKey, String.valueOf(this.getUid()));

			// Normalizing for backend
			switch(this.getTextMessageType()) {
			case sAllMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sAllType);
				
				break;
			case sInboxMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sInboxType);
				
				break;
			case sSentMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sSentType);
				
				break;
			case sDraftMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sDraftType);
				
				break;
			case sOutboxMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sOutboxType);
				
				break;
			case sFailedMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sFailedType);
				
				break;
			case sQueuedMessageType:
				textMessageJson.put(sTextMessageTypeJsonKey, sQueuedType);
				
				break;
			}
			
			textMessageJson.put(sThreadIdJsonKey, this.getThreadId());
			textMessageJson.put(sPhoneNumberJsonKey, this.getPhoneNumber());
			textMessageJson.put(sBodyJsonKey, this.getBody());
			// Normalizing for backend
			textMessageJson.put(sTimeJsonKey, this.getTime() / 1000);
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
