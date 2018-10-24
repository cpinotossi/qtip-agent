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


public class TestAgentPublishToCatchpoint extends MyTestParameters {

	/* Example cmd
	 * Subscribe to a topic
	 * -c client1 -a "measures:sub" -t "catchpoint/test"
	 * Publish to the same topic
	 * -c client2 -a "measures:pub" -t "catchpoint/test" -p -m "huhu"
	 * 
	 */
	@Before
	public void before() throws Exception {
		System.out.println("START TestAgentPublishToCatchpoint");
	}

	@Test
	public void test() {
		String jwt = null;

		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		try {
			System.out.println("AuthGroup: measures:pub");
			System.out.println("ID: client1");
			System.out.println("URL: "+Broker.getURI(Jurisdiction.EU));
			jwt = jwtBuilder
					.setAuthGroups(new String[]{"measures:pub"})
					.setClientId("client1")
					.setSigningKey(this.getKeyPrivate())
					.build();

			ClientBuilder clientBuilder = new ClientBuilder();
			MqttClient client = clientBuilder
					.addAuthGroups(new String[]{"measures:pub"})
					.setClientId("client1")
					.setBrokerURI(Broker.getURI(Jurisdiction.EU))
					.setJWT(jwt)
					.build();

client.connect();
for(int i=0;i<10;i++) {
	String msg = "hello "+Integer.toString(i);
	String topic = "catchpoint/test";
	System.out.println("topic: "+topic + "; message: "+msg);
	client.publish(topic, msg.getBytes(), 2, false);	
}
for(int i=0;i<10;i++) {
	String msg = "hello "+Integer.toString(i);
	String topic = "catchpoint/test/"+Integer.toString(i);
	System.out.println("topic: "+topic + "; message: "+msg);
	client.publish(topic, msg.getBytes(), 2, false);	
}
client.disconnect();
			System.out.println("jwt: " + jwt);
			assertNotNull("Verify that JWT not null", jwt);
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

		
		
	}

}
