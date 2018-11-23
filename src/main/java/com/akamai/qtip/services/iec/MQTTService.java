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
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.util.Debug;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import java.util.logging.Logger;

public class MQTTService {
	// private static final Logger logger =
	// LogManager.getLogger(EdgercService.class);

	public void publish(String clientIdName, String authGroupName, String clientId, String authGroup, String topic,
			String message, String pathtothekey, URI uri) throws Exception {
		this.publish(clientIdName, authGroupName, clientId, authGroup, topic, message, pathtothekey, 1, uri);
	}

	public void publish(String clientIdName, String authGroupName, String clientId, String authGroup, String topic,
			String message, String pathtothekey, int repeat, URI uri) throws Exception {
		System.out.println("START publish");
		System.out.println("Client ID\t:"+ clientId);
		System.out.println("Auth Group\t:"+authGroup);
		System.out.println("Topic:\t"+topic);
		System.out.println("Host:\t"+uri);
		System.out.println("Message:\t"+message);
		System.out.println("----------------------");
		System.out.println("STATUS: init Client");
		MqttClient client = getClient(clientIdName, authGroupName, clientId, authGroup, pathtothekey, uri, null);
		try {
			System.out.println("STATUS: try to connect");
			client.connect();
			System.out.println("STATUS: connected");
			Instant instant = Instant.now();
			long timeStampMillis = instant.toEpochMilli();
			String msg = "";

			for (int i = 0; i < repeat; ++i) {
				msg = "[" + timeStampMillis + "|" + clientId + "|" + i + "]: " + message;
				System.out.println("MESSAGE TO " + topic +  ": " + msg);
				client.publish(topic, msg.getBytes(), 2, false);
			}
			client.disconnect();
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			Logger.getLogger(this.getClass().getName()).severe(e.toString());
			e.printStackTrace();

		} finally {
			System.exit(0);
		}
	}

	public void subscribe(String clientIdName, String authGroupName, String clientId, String authGroup, String topic,
			String pathtothekey, URI uri) throws Exception{
		// callback functions defined, when certain MQTT events take place
		MqttCallback callback = new MqttCallback() {
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println("MESSAGE FROM " + topic + ": " + new String(message.getPayload()));
				// System.exit(0);
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
		System.out.println("START subscribe");
		System.out.println("Client ID:\t"+ clientId);
		System.out.println("Auth Group:\t"+authGroup);
		System.out.println("Topic:\t"+topic);
		System.out.println("Host:\t"+uri);
		System.out.println("----------------------");
		System.out.println("STATUS: init Client");
		MqttClient client = getClient(clientIdName, authGroupName, clientId, authGroup, pathtothekey, uri, callback);
		try {
			System.out.println("STATUS: try to connect");
			client.connect();
			System.out.println("STATUS: connected to "+client.getCurrentServerURI());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("STATUS: subscribe");
		client.subscribe(topic);
	}
	
	private MqttClient getClient(String clientIdName, 
			String authGroupName, 
			String clientId, 
			String authGroup,
			String pathtothekey, 
			URI uri, 
			MqttCallback callback){
		MqttClient client = null;
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		try {
			jwtBuilder.setAuthGroups(authGroupName, new String[] { authGroup }).setClientId(clientIdName, clientId)
					.setSigningKey(this.getKey(pathtothekey));
		} catch (IOException e) {
			System.out.println("ERROR: Key file does not exist");
			e.printStackTrace();
		}
		String jwt = jwtBuilder.build();
		System.out.println("JWT:\t"+jwt);
		ClientBuilder clientBuilder = new ClientBuilder();
			try {
				if(callback ==null) {
					client = clientBuilder.addAuthGroup(authGroup).setClientId(clientId)
							.setBrokerURI(uri).setJWT(jwt).build();
				}else {
					client = clientBuilder.addAuthGroup(authGroup).setCallback(callback).setClientId(clientId)
							.setBrokerURI(uri).setJWT(jwt).build();					
				}
			} catch (Exception e) {
				System.out.println("ERROR: Cannot create Mqtt client");
				e.printStackTrace();
			}

		return client;
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