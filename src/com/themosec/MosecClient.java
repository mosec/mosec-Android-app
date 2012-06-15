package com.themosec;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class MosecClient {
	private static AsyncHttpClient sHttpClient;
	private static PersistentCookieStore sCookieStore;
	
	// Must call this before calling any other method
	public static void setup(Context context) {
		sHttpClient = new AsyncHttpClient();
		
		sCookieStore = new PersistentCookieStore(context);
		
		sHttpClient.setCookieStore(sCookieStore);
	}
	
	public static String getAbsoluteUrl(String relativeUrl) {
		String url = EnvironmentHelper.getUrl() + relativeUrl;
		
		return url;
	}
	
	public static void get(String relativeUrl, AsyncHttpResponseHandler responseHandler) {
		String url = getAbsoluteUrl(relativeUrl);
		
		sHttpClient.get(url, responseHandler);
	}
	
	public static void post(String relativeUrl, RequestParams parameters, AsyncHttpResponseHandler responseHandler) {
		String url = getAbsoluteUrl(relativeUrl);
		
		sHttpClient.post(url, parameters, responseHandler);
	}
	
	public static void put(String relativeUrl, RequestParams parameters, AsyncHttpResponseHandler responseHandler) {
		String url = getAbsoluteUrl(relativeUrl);
		
		sHttpClient.put(url, parameters, responseHandler);
	}
	
	public static void delete(String relativeUrl, AsyncHttpResponseHandler responseHandler) {
		String url = getAbsoluteUrl(relativeUrl);
		
		sHttpClient.delete(url, responseHandler);
	}
	
	public static void clearCookies() {
		sCookieStore.clear();
	}
}
