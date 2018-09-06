package com.akamai.qtip;

import java.io.IOException;
import java.net.URI;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.ClientFactory;

public class Test {

	public static void main(String[] args) throws Exception {
		// A new JWT is generated for each connection. The signing key is provided via
		// environment.
		// The key must be PEM encoded.

		if (System.getenv("JWT_SIGNING_KEY") == null) {
			throw new Exception("Expects JWT_SIGNING_KEY env var to be set.");
		}

		URI broker = new URI("ssl://mqtt.hogg.fr:8883");
		String clientId = "qtip-agent-one";
		String[] authGroups = new String[] {"chat:pub", "chat:sub"};
		MqttClient client = ClientFactory.getIECClient(broker, clientId, authGroups);

		MqttMessage msg = new MqttMessage(("hello from"+clientId).getBytes());
		msg.setQos(1);
		client.publish("eu/chat", msg);
	}
}