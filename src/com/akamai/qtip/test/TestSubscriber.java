package com.akamai.qtip.test;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.Broker;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;


public class TestSubscriber {

	public static void main(String[] args) throws Exception {
		// A new JWT is generated for each connection. The signing key is provided via
		// environment.
		// The key must be PEM encoded.

		if (System.getenv("JWT_SIGNING_KEY") == null) {
			throw new Exception("Expects JWT_SIGNING_KEY env var to be set.");
		}

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

		ClientBuilder clientBuilder = new ClientBuilder();
		MqttClient client = clientBuilder.addAuthGroup("chatter:sub")
			.setClientId("qtipSubOne")
			.setBrokerURI(Broker.getURI(Jurisdiction.EU))
			.setCallback(callback)
			.build();

		client.connect();
		client.subscribe("chatter");
	}
}