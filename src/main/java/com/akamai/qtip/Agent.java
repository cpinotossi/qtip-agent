package com.akamai.qtip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.Messages.*;
import com.akamai.qtip.mqtt.AsyncPublisher;
import com.akamai.qtip.mqtt.iec.Jurisdiction;
import com.akamai.qtip.publishtest.PublishTestManager;
import com.akamai.qtip.subscribetest.SubscribeTestManager;
import com.google.protobuf.Any;
import com.google.protobuf.Message;

public class Agent implements Runnable, MqttCallback {
	private MqttClient mqtt;
	private AsyncPublisher chatterPublisher;
	private AsyncPublisher beaconPublisher;
	private Map<String, List<MessageHandler<?>>> messageHandlers = new HashMap<String, List<MessageHandler<?>>>();

	public Agent() throws Exception {
		mqtt = IEC.mqttClient(getJurisdiction(), new String[] {"chatter:sub", "measures:sub"});
		chatterPublisher = new AsyncPublisher(getJurisdiction(),new String[] {"measures:pub"}, Topic.CHATTER);
		beaconPublisher = new AsyncPublisher(getJurisdiction(),new String[] {"chatter:pub"}, Topic.BEACONS);
		setupHello();
		setupPublishTest();
		setupSubscribeTest();
		setupAggregator();
	}

	private void registerHandler(MessageHandler<?> handler) {
		List<MessageHandler<?>> handlers = messageHandlers.get(handler.getTypeUrl());
		if (handlers != null) {
			handlers.add(handler);
		} else {
			List<MessageHandler<?>> list = new ArrayList<MessageHandler<?>>();
			list.add(handler);
			messageHandlers.put(handler.getTypeUrl(), list);
		}
	}	
	
	private void setupHello() {
		registerHandler(new MessageHandler<Who>() {
			@Override
			protected void handle(AgentDescriptor sender, Who message) throws Exception {
				publishChatter(Hello.newBuilder().build());
			}
		});
	}
	
	private void setupPublishTest() {
		PublishTestManager manager = new PublishTestManager(this);
		registerHandler(new MessageHandler<StartTest>() {
			@Override
			protected void handle(AgentDescriptor sender, StartTest message) throws Exception {
				// TODO: return if the message is not for this agent
				manager.startTest(message);
			}
		});
		registerHandler(new MessageHandler<AbortTest>() {
			@Override
			protected void handle(AgentDescriptor sender, AbortTest message) throws Exception {
				manager.abortTest(message.getTestId());
			}
		});
	}

	private void setupSubscribeTest() {
		SubscribeTestManager manager = new SubscribeTestManager(this);
		registerHandler(new MessageHandler<StartTest>() {
			@Override
			protected void handle(AgentDescriptor sender, StartTest message) throws Exception {
				manager.startTest(message);
			}
		});
		registerHandler(new MessageHandler<AbortTest>() {
			@Override
			protected void handle(AgentDescriptor sender, AbortTest message) throws Exception {
				manager.abortTest(message.getTestId());
			}
		});
		// TODO abort on CLOSE TEST message
	}

	private void setupAggregator() {
		// TODO Auto-generated method stub
		
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

	public void publishChatter(Message message) {
		chatterPublisher.publish(envelop(message));
	}

	public void publishBeacon(Message message) {
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
		Any payload = e.getPayload();
		List<MessageHandler<?>> handlers = messageHandlers.get(payload.getTypeUrl());
		for (MessageHandler<?> handler : handlers) {
			handler.handle(e.getSender(), payload);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
	}
}
