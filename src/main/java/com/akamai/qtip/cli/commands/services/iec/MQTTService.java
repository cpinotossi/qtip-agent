package com.akamai.qtip.cli.commands.services.iec;

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
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService {
	public void publish(String clientId, String authGroup, String topic, String message, String pathtothekey, URI uri)
			throws Exception {
		this.publish(clientId, authGroup, topic, message, pathtothekey, 1, uri);
	}

	public void publish(String clientId, String authGroup, String topic, String message, String pathtothekey,
			int repeat, URI uri) throws Exception {
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		jwtBuilder.setAuthGroups(new String[]{authGroup}).setSigningKey(this.getKey(pathtothekey));
		String jwt = jwtBuilder.build();
		ClientBuilder clientBuilder = new ClientBuilder();
		MqttClient client = clientBuilder.addAuthGroup(authGroup).setClientId(clientId).setBrokerURI(uri).setJWT(jwt)
				.build();
		System.out.println("START publish");
		System.out.println(clientId);
		System.out.println(authGroup);
		System.out.println(topic);
		System.out.println(uri);
		System.out.println(jwt);
		System.out.println("connect");
		client.connect();
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli();
		String msg = "[" + timeStampMillis + "]:" + message;

		for (int i = 0; i < repeat; ++i) {
			System.out.println("Repeat#" + i + " msg:" + msg);
			client.publish(topic, msg.getBytes(), 2, false);
		}

		client.disconnect();
		System.exit(0);
	}


    
	public void subscribe(String clientId, String authGroup, String topic, String pathtothekey, URI uri) throws Exception {
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
      jwtBuilder.setAuthGroups(new String[]{authGroup}).setSigningKey(this.getKey(pathtothekey));
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