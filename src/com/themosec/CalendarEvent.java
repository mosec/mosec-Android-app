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

public class CalendarEvent {
	public static final Uri CALENDARS_URI = Uri.parse("content://com.android.calendar/calendars");
	public static final Uri CALENDAR_EVENTS_URI = Uri.parse("content://com.android.calendar/events");
	public static final Uri CALENDAR_EVENT_ATTENDEES_URI = Uri.parse("content://com.android.calendar/attendees");
	
	public static final String TYPE = "calendar_event";
	
	private static final String sUidJsonKey = "uid";
	private static final String sTitleJsonKey = "title";
	private static final String sDescriptionJsonKey = "description";
	private static final String sLocationJsonKey = "location";
	private static final String sAttendeeEmailAddressesJsonKey = "attendee_email_addresses";
	private static final String sStartTimeJsonKey = "start_time";
	private static final String sEndTimeJsonKey = "end_time";
	private static final String sAllDayJsonKey = "all_day";
	
	private long mUid;
	private String mTitle;
	private String mDescription;
	private String mLocation;
	private String mAttendeeEmailAddresses;
	private long mStartTime;
	private long mEndTime;
	private boolean mAllDay;
	
	public CalendarEvent() {
		mUid = -1;
		mTitle = null;
		mDescription = null;
		mLocation = null;
		mAttendeeEmailAddresses = null;
		mStartTime = -1;
		mEndTime = -1;
		mAllDay = false;
	}
	
	public long getUid() {
		return mUid;
	}
	
	public void setUid(long uid) {
		mUid = uid;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getDescription() {
		return mDescription;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
	public String getLocation() {
		return mLocation;
	}
	
	public void setLocation(String location) {
		mLocation = location;
	}
	
	public String getAttendeeEmailAddresses() {
		return mAttendeeEmailAddresses;
	}
	
	public void setAttendeeEmailAddresses(String attendeeEmailAddresses) {
		mAttendeeEmailAddresses = attendeeEmailAddresses;
	}
	
	public long getStartTime() {
		return mStartTime;
	}
	
	public void setStartTime(long startTime) {
		mStartTime = startTime;
	}
	
	public long getEndTime() {
		return mEndTime;
	}
	
	public void setEndTime(long endTime) {
		mEndTime = endTime;
	}
	
	public boolean getAllDay() {
		return mAllDay;
	}
	
	public void setAllDay(boolean allDay) {
		mAllDay = allDay;
	}
	
	public JSONObject toJson() {
		JSONObject calendarEventJson = new JSONObject();
		
		try {
			// Normalizing for backend
			calendarEventJson.put(sUidJsonKey, String.valueOf(this.getUid()));
			calendarEventJson.put(sTitleJsonKey, this.getTitle());
			calendarEventJson.put(sDescriptionJsonKey, this.getDescription());
			calendarEventJson.put(sLocationJsonKey, this.getLocation());
			calendarEventJson.put(sAttendeeEmailAddressesJsonKey, this.getAttendeeEmailAddresses());
			// Normalizing for backend
			if(this.getStartTime() == -1) {
				calendarEventJson.put(sStartTimeJsonKey, this.getStartTime());
			} else {
				calendarEventJson.put(sStartTimeJsonKey, this.getStartTime() / 1000);
			}
			// Normalizing for backend
			if(this.getEndTime() == -1) {
				calendarEventJson.put(sEndTimeJsonKey, this.getEndTime());
			} else {
				calendarEventJson.put(sEndTimeJsonKey, this.getEndTime() / 1000);
			}
			calendarEventJson.put(sAllDayJsonKey, this.getAllDay());
		} catch(JSONException exception) {
			
		}
		
		return calendarEventJson;
	}
	
	public static List<CalendarEvent> getAll(ContentResolver contentResolver) {
		Cursor calendarsCursor = contentResolver.query(CALENDARS_URI, new String[] { "_id" }, null, null, null);

		List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
		
		while(calendarsCursor.moveToNext()) {
			String calendarId = calendarsCursor.getString(0);
		
			Cursor calendarEventsCursor = contentResolver.query(CALENDAR_EVENTS_URI, new String[] { "_id", "title", "description", "eventLocation", "dtstart", "dtend", "allDay" }, "calendar_id = " + calendarId, null, null);
			
			while(calendarEventsCursor.moveToNext()) {
				long uid = calendarEventsCursor.getLong(0);
				String title = calendarEventsCursor.getString(1);
				String description = calendarEventsCursor.getString(2);
				String location = calendarEventsCursor.getString(3);
				long startTime = calendarEventsCursor.getLong(4);
				long endTime = calendarEventsCursor.getLong(5);
				boolean allDay = !calendarEventsCursor.getString(6).equals("0");
				
				Cursor calendarEventAttendeesCursor = contentResolver.query(CALENDAR_EVENT_ATTENDEES_URI, new String[] { "attendeeEmail" }, "event_id = " + uid, null, null);
				
				String attendeeEmailAddresses = null;
				
				while(calendarEventAttendeesCursor.moveToNext()) {
					String attendeeEmailAddress = calendarEventAttendeesCursor.getString(0);
					
					if(attendeeEmailAddresses == null) {
						attendeeEmailAddresses = attendeeEmailAddress;
					} else {
						attendeeEmailAddresses = attendeeEmailAddresses + "," + attendeeEmailAddress;
					}
				}
				
				CalendarEvent calendarEvent = new CalendarEvent();
				
				calendarEvent.setUid(uid);
				calendarEvent.setTitle(title);
				calendarEvent.setDescription(description);
				calendarEvent.setLocation(location);
				calendarEvent.setAttendeeEmailAddresses(attendeeEmailAddresses);
				calendarEvent.setStartTime(startTime);
				calendarEvent.setEndTime(endTime);
				calendarEvent.setAllDay(allDay);
				
				calendarEvents.add(calendarEvent);
				
				calendarEventAttendeesCursor.close();
			}
			
			calendarEventsCursor.close();
		}
		
		return calendarEvents;
	}
	
	public static JSONArray toJsonFromCalendarEvents(List<CalendarEvent> calendarEvents) {
		Iterator<CalendarEvent> calendarEventsIterator = calendarEvents.iterator();
		
		JSONArray calendarEventsJson = new JSONArray();
		
		while(calendarEventsIterator.hasNext()) {
    		CalendarEvent calendarEvent = calendarEventsIterator.next();
			
			JSONObject calendarEventJson = calendarEvent.toJson();
			
			calendarEventsJson.put(calendarEventJson);
		}
		
		return calendarEventsJson;
	}
}
