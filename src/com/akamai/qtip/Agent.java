package com.akamai.qtip;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.Messages.Envelope;
import com.akamai.qtip.Messages.PublisherBeacon;
import com.akamai.qtip.mqtt.AsyncPublisher;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;
import com.google.protobuf.Any;

public class Agent implements Runnable, MqttCallback {

	private MqttClient mqttClient;

	public Agent() throws Exception {
		mqttClient = createMqttClient();
	}

	private MqttClient createMqttClient() throws Exception {
		ClientBuilder clientBuilder = new ClientBuilder();
		clientBuilder.setBrokerURI(Broker.getURI(getJurisdiction()))
			.setCallback(this)
			.addAuthGroup("chatter:pub")
			.addAuthGroup("chatter:sub")
			.addAuthGroup("measures:pub")
			.addAuthGroup("measures:sub");
		return clientBuilder.build();
	}

	public Jurisdiction getJurisdiction() {
		// FIXME: select this based on DNS? GTM?
		return Jurisdiction.EU;
	}

	public MqttClient getMqttClient() {
		return mqttClient;
	}

	@Override
	public void run() {
		try {
			mqttClient.connect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(this.getClass().getName()).fine("Timed out waiting for a beacon to send...");
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		Logger.getLogger(this.getClass().getName()).severe("Lost connection...");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Envelope e = Envelope.parseFrom(message.getPayload());
		Any payload = e.getPayload();
		if (payload.is(PublisherBeacon.class)) {
			PublisherBeacon pb = payload.unpack(PublisherBeacon.class);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
	}
}
