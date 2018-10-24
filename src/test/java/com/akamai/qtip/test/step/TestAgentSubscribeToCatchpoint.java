package com.akamai.qtip.test.step;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;

import com.akamai.qtip.Broker;
import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;

public class TestAgentSubscribeToCatchpoint extends MyTestParameters {

	//-c client2 -a "measures:pub" -t "catchpoint/test" -p -m "hello"
	@Before
	public void before() throws Exception {
		System.out.println("START TestAgentSubscribeToCatchpoint");
	}

	@Test
	public void test() throws URISyntaxException {
		String jwt = null;

		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		try {
			System.out.println("AuthGroup: measures:sub");
			System.out.println("ID: client2");
			System.out.println("URL: "+Broker.getURI(Jurisdiction.EU));
			jwt = jwtBuilder
					.setAuthGroups(new String[]{"measures:sub"})
					.setClientId("client2")
					.setSigningKey(this.getKeyPrivate())
					.build();

			// callback functions defined, when certain MQTT events take place
			MqttCallback callback = new MqttCallback() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("MESSAGE FROM " + topic + ": " + new String(message.getPayload())
							+ " with this qos: " + Integer.toString(message.getQos()));
					System.exit(0);
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
				}

				@Override
				public void connectionLost(Throwable cause) {
					System.out.println("Exception " + cause);
					System.exit(0);
				}
			};

			ClientBuilder clientBuilder = new ClientBuilder();
			MqttClient client;
			try {
				client = clientBuilder
						.addAuthGroups(new String[]{"measures:sub"})
						.setCallback(callback)
						.setClientId("client2")
						.setBrokerURI(Broker.getURI(Jurisdiction.EU))
						.setJWT(jwt)
						.build();
				client.connect();
				client.subscribe("catchpoint/*");
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		System.out.println("jwt: " + jwt);
		assertNotNull("Verify that JWT not null", jwt);
	}

}
