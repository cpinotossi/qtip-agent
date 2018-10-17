package com.akamai.qtip.cli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.Broker;
import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;
//import com.akamai.edgeauth.AkamaiTokenConfig;
//import com.akamai.edgeauth.AkamaiTokenGenerator;
import com.beust.jcommander.JCommander;

//TODO ota api support
public class QtipCLI {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		if (null == args || args.length == 0) {
			System.out.println("try '--help' or '-h' for more information");
			System.exit(1);
		}
		// Read user input parameter.
		Commands commands = new Commands();
		JCommander jc = new JCommander();
		jc.addObject(commands);
		try {
			jc.parse(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			jc.usage();
			System.exit(1);
		}

		QtipCLI cli = new QtipCLI();
		if (commands.publish) {
			cli.publish(commands.clientid, commands.authgroup, commands.topic, commands.message, commands.key);
		} else {
			cli.subscribe(commands.clientid, commands.authgroup, commands.topic, commands.key);
		}
	}

	public void publish(String clientId, String authGroup, String topic, String message, String pathtothekey) throws Exception {
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		jwtBuilder.setAuthGroups(new String[] { authGroup }).setSigningKey(getKey(pathtothekey));
		String jwt = jwtBuilder.build();

		ClientBuilder clientBuilder = new ClientBuilder();
		MqttClient client = clientBuilder.addAuthGroup(authGroup)
				.setClientId(clientId)
				.setBrokerURI(Broker.getURI(Jurisdiction.EU))
				.setJWT(jwt)
				.build();

		
		System.out.println("START publish");
		System.out.println(clientId);
		System.out.println(authGroup);
		System.out.println(topic);
		System.out.println(Jurisdiction.EU.toString());
		System.out.println(jwt);
		System.out.println("connect");
		
		client.connect();
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli();
		String msg = "["+timeStampMillis+"]:"+message;
		client.publish(topic, msg.getBytes(), 2, false);
		client.disconnect();
		System.exit(0);
	}

	public void subscribe(String clientId, String authGroup, String topic, String pathtothekey) throws Exception {
		// callback functions defined, when certain MQTT events take place
		MqttCallback callback = new MqttCallback() {
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println("MESSAGE FROM " + topic + ": " + new String(message.getPayload())
						+ " with this qos: " + Integer.toString(message.getQos()));
				//System.exit(0);
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}

			@Override
			public void connectionLost(Throwable cause) {
				System.out.println("Exception " + cause);
				System.exit(0);
			}
		};
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		jwtBuilder.setAuthGroups(new String[] { authGroup }).setSigningKey(getKey(pathtothekey));
		String jwt = jwtBuilder.build();
		

		ClientBuilder clientBuilder = new ClientBuilder();
		MqttClient client = clientBuilder.addAuthGroup(authGroup).setCallback(callback).setClientId(clientId)
				.setBrokerURI(Broker.getURI(Jurisdiction.EU)).setJWT(jwt).build();

		System.out.println("START subscribe");
		System.out.println(clientId);
		System.out.println(authGroup);
		System.out.println(topic);
		System.out.println(Jurisdiction.EU.toString());
		System.out.println(jwt);
		System.out.println("connect");

		client.connect();
		client.subscribe(topic);
	}
	
	private static String getKey(String filename) throws IOException {
	    // Read key from file
	    String strKeyPEM = "";
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = br.readLine()) != null) {
	        strKeyPEM += line + "\n";
	    }
	    br.close();
	    return strKeyPEM;
	}
	
	

}
