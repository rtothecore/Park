package com.NTS.Network;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class ResponseGetter {
	
	List<NameValuePair> params;

	public ResponseGetter(List<NameValuePair> params) {
		this.params = params;
	}

	public String get(String url) {
		String response = "";
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			HttpClient httpclient = new DefaultHttpClient();
			UrlEncodedFormEntity entity = null;
			entity = new UrlEncodedFormEntity(params, "euc-kr");
			HttpPost post = new HttpPost(url);
			post.setEntity(entity);
			response = httpclient.execute(post, responseHandler);
		} 
		catch(Exception e) {}
		return response;
	}
	
}