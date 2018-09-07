package com.akamai.qtip;

import java.net.URI;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.mqtt.ClientFactory;

public class TestSubscriber {

	public static void main(String[] args) throws Exception {
		// A new JWT is generated for each connection. The signing key is provided via
		// environment.
		// The key must be PEM encoded.

		if (System.getenv("JWT_SIGNING_KEY") == null) {
			throw new Exception("Expects JWT_SIGNING_KEY env var to be set.");
		}

		URI broker = new URI("ssl://qtip-eu.a2s.ninja:8883");
		String clientId = "qtipSubOne";
		String[] authGroups = new String[] {"chatter:sub"};
		MqttClient client = ClientFactory.getIECClient(broker, clientId, authGroups);

		//callback functions defined, when certain MQTT events take place
        MqttCallback callback = new MqttCallback() {
        	@Override
        	public void messageArrived(String topic, MqttMessage message) throws Exception {
        		System.out.println("MESSAGE FROM "+topic+": " +
	            new String(message.getPayload())+" with this qos: " +
	            Integer.toString(message.getQos()));
				System.exit(0);
        	}

        	@Override
        	public void deliveryComplete(IMqttDeliveryToken token) { }

        	@Override
        	public void connectionLost(Throwable cause) {
        				System.out.println("Exception "+cause);
        				System.exit(0);
        	}
        };
        
        client.setCallback(callback);      
		client.subscribe("chatter");
	}
}