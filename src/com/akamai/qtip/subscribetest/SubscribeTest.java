package com.akamai.qtip.subscribetest;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.akamai.qtip.Agent;
import com.akamai.qtip.Messages.StartSubscribe;
import com.akamai.qtip.Messages.StartTest;
import com.akamai.qtip.Messages.SubscriberBail;
import com.akamai.qtip.Messages.SubscriberBeacon;
import com.akamai.qtip.mqtt.iec.ClientBuilder;

public class SubscribeTest implements Runnable {
	private Agent agent;
	private SubscribeTestManager manager;
	private MqttClient client;
	private StartTest parameters;
	
	public SubscribeTest(Agent agent, SubscribeTestManager manager, StartTest parameters) throws URISyntaxException, Exception {
		this.agent = agent;
		this.parameters = parameters;
		this.client = new ClientBuilder()
				.setBrokerURI(new URI(parameters.getBrokerUri()))
				.setCallback(new MqttCallback() {
					@Override
					public void connectionLost(Throwable cause) {
						bail(cause);
					}
					@Override
					public void messageArrived(String topic, MqttMessage message) throws Exception {
						onMessageArrived(topic, message);
					}
					@Override
					public void deliveryComplete(IMqttDeliveryToken token) {
						// UNUSED: no message sent by subscribe test
					}
				})
				.build();
	}
	
	public void close() {
		try {
			client.disconnectForcibly();
		} catch (MqttException e) {
		}
		try {
			client.close();
		} catch (MqttException e) {
		}
	}
	
	private void onMessageArrived(String topic, MqttMessage message) {
		long now = System.currentTimeMillis();
		agent.publishBeacon(
				SubscriberBeacon
				.newBuilder()
				.setTestId(parameters.getTestId())
				.setDoneTime(now)
				.build());
		
	}
	private void bail(Throwable cause) {
		agent.publishChatter(
				SubscriberBail
				.newBuilder()
				.setTestId(parameters.getTestId())
				.build());
		try {
			client.close();
		} catch (MqttException e) {
		}
		manager.remove(parameters.getTestId());
	}

	@Override
	public void run() {
		try {
			client.connect();
		} catch (MqttException e) {
			bail(e);
			return;
		}
		agent.publishChatter(
				StartSubscribe.newBuilder()
				.setTestId(parameters.getTestId())
				.build());
	}
}
