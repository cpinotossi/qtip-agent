package com.akamai.qtip;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.akamai.qtip.Messages.AgentDescriptor;
import com.akamai.qtip.Messages.Envelope;
import com.akamai.qtip.mqtt.AsyncPublisher;
import com.akamai.qtip.mqtt.iec.Jurisdiction;
import com.google.protobuf.Any;
import com.google.protobuf.Message;

public class Agent implements Runnable, MqttCallback {
	private MqttClient mqtt;
	private AsyncPublisher chatterPublisher;
	private AsyncPublisher beaconPublisher;


	public Agent() throws Exception {
		mqtt = IEC.mqttClient(getJurisdiction(), new String[] {"chatter:pub", "chatter:sub", "measures:pub", "measures:sub"});
		chatterPublisher = new AsyncPublisher(mqtt, Topic.CHATTER);
		beaconPublisher = new AsyncPublisher(mqtt, Topic.BEACONS);
	}

	public Jurisdiction getJurisdiction() {
		// FIXME: select jurisdiction based on DNS? GTM?
		return Jurisdiction.getDefault();
	}

	public AgentDescriptor getDescriptor() {
		return AgentDescriptor.newBuilder()
				.setClientId(mqtt.getClientId())
				// TODO: retrieve edgescape data from https://qtip.a2s.ninja/whereami
				.setLat(0)
				.setLon(0)
				.build();
	}

	private Envelope envelop(Message message) {
		return Envelope.newBuilder()
				.setSender(getDescriptor())
				.setPayload(Any.pack(message))
				.build();
	}

	public void publishChatter(Message message) throws MqttException {
		chatterPublisher.publish(envelop(message));
	}

	public void publishBeacon(Message message) throws MqttException {
		beaconPublisher.publish(envelop(message));
	}

	@Override
	public void run() {
		mqtt.setCallback(this);
//		if (this.isAggregator()) {
//			this.startAggregator(); // new Aggregator(this);
//		}
		try {
			mqtt.subscribe(Topic.CHATTER.toString());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Envelope e = Envelope.parseFrom(message.getPayload());
//		Any payload = e.getPayload();
//		if (payload.getClass() == StartTest) {
//			this.startTest();
//		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
}
