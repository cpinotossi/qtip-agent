package com.akamai.qtip.publishtest;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import com.akamai.qtip.Agent;
import com.akamai.qtip.Messages.StartTest;
import com.akamai.qtip.mqtt.iec.ClientBuilder;

public class PublishTest implements MqttCallback {
	private Agent agent;
	private MqttClient client;
	
	public PublishTest(Agent agent, StartTest parameters) throws URISyntaxException, Exception {
		this.agent = agent;
		this.client = new ClientBuilder()
				.setBrokerURI(new URI(parameters.getBrokerUri()))
				.setCallback(this)
				.build();
	}
	
	public void start() {
		// OPEN MQTT CONNECTION
		// PUBLISH MESSAGES
	}
	
	public void abort() {
		
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
}
