package com.akamai.qtip.mqtt;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.akamai.qtip.Messages.Envelope;
import com.akamai.qtip.Topic;

/**
 * Thread that consumes a queue of Envelope instances to be sent
 * to an Mqtt topic asynchronously.
 *
 * @author ahogg
 */
public class AsyncPublisher extends Thread {
	private MqttClient client;
	private LinkedBlockingQueue<Envelope> queue;
	private Topic topic;

	public AsyncPublisher(MqttClient client, Topic topic) throws Exception {
		this.client = client;
		this.setName(this.getClass().getName());
		this.topic = topic;
		this.queue = new LinkedBlockingQueue<Envelope>();
	}

	public Boolean publish(Envelope e) {
		return this.queue.offer(e);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Envelope e = this.queue.poll(1, TimeUnit.HOURS);
				client.publish(topic.toString(), e.toByteArray(), 2, false);
			} catch (InterruptedException e) {
				Logger.getLogger(this.getName()).fine("Timed out waiting for the message to send...");
				// FIXME: maybe publish an "I'm bored" message to chatter?
			} catch (MqttPersistenceException e) {
				// FIXME: not sure what to do here
				Logger.getLogger(this.getName()).severe(e.toString());
			} catch (MqttException e) {
				// FIXME: not sure what to do here
				Logger.getLogger(this.getName()).severe(e.toString());
			}
		}
	}
}
