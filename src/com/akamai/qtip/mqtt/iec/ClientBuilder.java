package com.akamai.qtip.mqtt.iec;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.akamai.qtip.mqtt.SocketFactory;

public class ClientBuilder {

	/**
	 * Code structure facility. The standard MqttClient requires the
	 * MqttConnectOptions to be specified as a parameter to the connect() method.
	 * 
	 * We want business logic to be able to create an MqttClient early, and
	 * connect() later without requiring knowledge of the implementation details of
	 * MqttConnectOptions for IEC.
	 * 
	 * @author ahogg
	 */
	private class MqttClientWithConnectOptions extends MqttClient {

		private MqttConnectOptions connectOptions;

		public MqttClientWithConnectOptions(String serverURI, String clientId) throws MqttException {
			super(serverURI, clientId);
		}

		public MqttClientWithConnectOptions(String serverURI, String clientId, MqttClientPersistence persistence)
				throws MqttException {
			super(serverURI, clientId, persistence);
		}

		public MqttClientWithConnectOptions setConnectOptions(MqttConnectOptions connectOptions) {
			this.connectOptions = connectOptions;
			return this;
		}

		@Override
		public void connect() throws MqttSecurityException, MqttException {
			connect(connectOptions);
		}
	}

	private URI brokerURI;
	private String clientId;
	private String jwt;
	private MqttClientPersistence persistence = new MemoryPersistence();
	private MqttCallback callback;
	private List<String> authGroups;

	public ClientBuilder() {
		this.authGroups = new ArrayList<String>();
	}

	public ClientBuilder setPersistence(MqttClientPersistence persistence) {
		this.persistence = persistence;
		return this;
	}

	public ClientBuilder setBrokerURI(URI brokerURI) {
		this.brokerURI = brokerURI;
		return this;
	}

	public ClientBuilder setClientId(String clientId) throws Exception {
		//if (!clientId.matches("[a-zA-Z0-9]{0,23}")) {
		//	throw new Exception(String.format("Invalid client id, expecting /[a-z0-9]{0,23}/, got %s", clientId));
		//}
		this.clientId = clientId;
		return this;
	}

	public ClientBuilder addAuthGroup(String authGroup) {
		this.authGroups.add(authGroup);
		return this;
	}

	public ClientBuilder setCallback(MqttCallback callback) {
		this.callback = callback;
		return this;
	}

	public ClientBuilder setJWT(String jwt) {
		this.jwt = jwt;	
		return this;
	}

	public MqttClient build() throws Exception {

		MqttClientWithConnectOptions client = new MqttClientWithConnectOptions(brokerURI.toString(), clientId,
				this.persistence);

		// update the connection with credentials
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setUserName(clientId); // this is ignored by IEC, we provide the clientId arbitrarily
		connectOptions.setPassword(jwt.toCharArray());

		// extract hostname from broker URL and create socket factory
		SSLSocketFactory ssf = SocketFactory.getSNISocketFactory(brokerURI.getHost());
		connectOptions.setSocketFactory(ssf);
		client.setConnectOptions(connectOptions);

		if (callback != null) {
			client.setCallback(callback);
		}

		return client;
	}

	public ClientBuilder addAuthGroups(String[] authGroups) {
		for (int i = 0; i < authGroups.length; i++) {
			this.addAuthGroup(authGroups[i]);
		}
		return this;
	}
}
