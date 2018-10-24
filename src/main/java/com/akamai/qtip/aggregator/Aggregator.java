package com.akamai.qtip.aggregator;

import java.io.ByteArrayOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class  Aggregator {
	//private Map<String, Aggregate> aggregates = new HashMap<String, Aggregate>();

	public int doPost(String url, String body) {
		//Create container to save post response.
		HttpEntity entity = null;
		HttpResponse response = null;
		System.out.println("post url: "+ url);
		try {
			//send POST request.
			System.out.println("post send data");
			response = Request.Post(url).useExpectContinue().version(HttpVersion.HTTP_1_1)
					.bodyString(body, ContentType.DEFAULT_TEXT).execute().returnResponse();
			entity = response.getEntity();
			//validate POST response.
			if (entity != null) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				entity.writeTo(byteArrayOutputStream);
				System.out.println("post response status code:" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			System.out.print("post:" + e.getMessage() + "\npost url:" + url);
			e.printStackTrace();
		}
		return response.getStatusLine().getStatusCode();			
	}
}
