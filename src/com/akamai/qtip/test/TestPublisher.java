package com.akamai.qtip.test;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.akamai.qtip.Broker;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;

public class TestPublisher {

	public static void main(String[] args) throws Exception {
		// A new JWT is generated for each connection. The signing key is provided via
		// environment.
		// The key must be PEM encoded.

		if (System.getenv("JWT_SIGNING_KEY") == null) {
			throw new Exception("Expects JWT_SIGNING_KEY env var to be set.");
		}

		ClientBuilder clientBuilder = new ClientBuilder();
		MqttClient client = clientBuilder.addAuthGroup("chatter:pub")
			.setClientId("qtipPubOne")
			.setBrokerURI(Broker.getURI(Jurisdiction.EU))
			.build();

		client.connect();
		client.publish("chatter", "hello!!!!".getBytes(), 2, false);
		client.disconnect();
        System.exit(0);
	}
}