package com.akamai.qtip.services.iec;

import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.util.Debug;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import java.util.logging.Logger;

public class MQTTService {
	//private static final Logger logger = LogManager.getLogger(EdgercService.class);
	
	public void publish(String clientIdName, String authGroupName, String clientId, String authGroup, String topic, String message, String pathtothekey, URI uri)
			throws Exception {
		this.publish(clientIdName, authGroupName, clientId, authGroup, topic, message, pathtothekey, 1, uri);
	}


	public void publish(String clientIdName, String authGroupName, String clientId, String authGroup, String topic, String message, String pathtothekey,
			int repeat, URI uri) throws Exception{
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		jwtBuilder.setAuthGroups(authGroupName ,new String[]{authGroup}).setSigningKey(this.getKey(pathtothekey));
		String jwt = jwtBuilder.build();
		ClientBuilder clientBuilder = new ClientBuilder();
		MqttClient client = clientBuilder.addAuthGroup(authGroup).setClientId(clientId).setBrokerURI(uri).setJWT(jwt)
				.build();
		Debug debug = client.getDebug();
		System.out.println("START publish");
		System.out.println(clientId);
		System.out.println(authGroup);
		System.out.println(topic);
		System.out.println(uri);
		System.out.println(jwt);
		System.out.println(message);
		System.out.println("connect");
		try {
			client.connect();
			Instant instant = Instant.now();
			long timeStampMillis = instant.toEpochMilli();
			String msg = "";

			for (int i = 0; i < repeat; ++i) {
				System.out.println("Repeat#" + i + " msg:" + msg);
				msg = "[" + timeStampMillis + " | #:" + i +"]:" + message;
				client.publish(topic, msg.getBytes(), 2, false);
			}
			client.disconnect();
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			
			//Logger.getLogger(this.getClass().getName()).fine("Timed out waiting for the message to send...");
			debug.dumpClientDebug();
			Logger.getLogger(this.getClass().getName()).severe(e.toString());
			e.printStackTrace();
			
		} finally{
			System.exit(0);
		}
	}


    
	public void subscribe(String clientIdName, String authGroupName, String clientId, String authGroup, String topic, String pathtothekey, URI uri) throws Exception {
		//callback functions defined, when certain MQTT events take place
	    MqttCallback callback = new MqttCallback() {
	    	@Override
	    	public void messageArrived(String topic, MqttMessage message) throws Exception {
	    		System.out.println("MESSAGE FROM "+topic+": " +
	            new String(message.getPayload())+" with this qos: " +
	            Integer.toString(message.getQos()));
				//System.exit(0);
	    	}

	    	@Override
	    	public void deliveryComplete(IMqttDeliveryToken token) { }

	    	@Override
	    	public void connectionLost(Throwable cause) {
	    				System.out.println("Exception "+cause);
	    				System.exit(0);
	    	}
	    };
      IECJWTBuilder jwtBuilder = new IECJWTBuilder();
      jwtBuilder.setAuthGroups(authGroupName,new String[]{authGroup}).setSigningKey(this.getKey(pathtothekey));
      String jwt = jwtBuilder.build();
      ClientBuilder clientBuilder = new ClientBuilder();
      MqttClient client = clientBuilder.addAuthGroup(authGroup).setCallback(callback).setClientId(clientId).setBrokerURI(uri).setJWT(jwt).build();
      System.out.println("START subscribe");
      System.out.println(clientId);
      System.out.println(authGroup);
      System.out.println(topic);
      System.out.println(uri);
      System.out.println(jwt);
      System.out.println("connect");
      client.connect();
      client.subscribe(topic);
   }

	private String getKey(String filename) throws IOException {
		String strKeyPEM = "";

		BufferedReader br;
		String line;
		for (br = new BufferedReader(new FileReader(filename)); (line = br.readLine()) != null; strKeyPEM = strKeyPEM
				+ line + "\n") {
			;
		}

		br.close();
		return strKeyPEM;
	}
}