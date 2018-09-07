package com.akamai.qtip;

import java.net.URI;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.akamai.qtip.mqtt.ClientFactory;

public class TestPublisher {

	public static void main(String[] args) throws Exception {
		// A new JWT is generated for each connection. The signing key is provided via
		// environment.
		// The key must be PEM encoded.

		if (System.getenv("JWT_SIGNING_KEY") == null) {
			throw new Exception("Expects JWT_SIGNING_KEY env var to be set.");
		}

		URI broker = new URI("ssl://qtip-eu.a2s.ninja:8883");
		String clientId = "qtipPubOne";
		String[] authGroups = new String[] {"chatter:pub"};
		MqttClient client = ClientFactory.getIECClient(broker, clientId, authGroups);

		client.publish("chatter", "hello!!!!".getBytes(), 2, false);
		client.disconnect();
        System.exit(0);
	}
}