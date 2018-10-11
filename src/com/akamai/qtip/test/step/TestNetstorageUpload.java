package com.akamai.qtip.test.step;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.time.Instant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

public class TestNetstorageUpload extends MyTestParameters {

	@Before
	public void before() {

	}

	@Test
	public void test() {
		//get current timestamp
		Instant instant = Instant.now();
		String timeStampMillis = String.valueOf(instant.toEpochMilli());
		//create POST url
		String url = "http://"+ this.getHostname() 
		+ this.getUploadPathRoot() 
		+ "/"
		+ timeStampMillis
		+ ".dat";
		//Create container to save post response.
		HttpEntity entity = null;
		HttpResponse response = null;
		System.out.println("post url: "+ url);
		try {
			//send POST request.
			System.out.println("post send data");
			response = Request.Post(url).useExpectContinue().version(HttpVersion.HTTP_1_1)
					.bodyString(this.getUploadBody(), ContentType.DEFAULT_TEXT).execute().returnResponse();
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
		assertTrue(response.getStatusLine().getStatusCode() == 200);			
	}

}
