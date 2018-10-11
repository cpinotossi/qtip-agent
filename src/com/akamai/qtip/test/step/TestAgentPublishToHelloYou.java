package com.akamai.qtip.test.step;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.Before;
import org.junit.Test;

import com.akamai.qtip.Broker;
import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;

public class TestAgentPublishToHelloYou extends MyTestParameters {

	@Before
	public void before() throws Exception {

	}

	@Test
	public void test() {
		String jwt = null;

		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		try {
			jwt = jwtBuilder
					.setAuthGroups(this.getAgentAuthGroup())
					.setClientId(this.getAgentID())
					.setSigningKey(this.getKeyPrivate())
					.build();

			ClientBuilder clientBuilder = new ClientBuilder();
			MqttClient client = clientBuilder
					.addAuthGroups(this.getAgentAuthGroup())
					.setClientId(this.getAgentID())
					.setBrokerURI(Broker.getURI(Jurisdiction.US))
					.setJWT(jwt)
					.build();

			client.connect();
			String msg = this.getAgentHelloYouMessage();
			client.publish(this.getTopicHelloYou(), msg.getBytes(), 2, false);
			client.disconnect();
	        System.exit(0);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("jwt: " + jwt);

		assertNotNull("Verify that JWT not null", jwt);
	}

}
