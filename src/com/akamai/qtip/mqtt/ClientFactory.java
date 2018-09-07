package com.akamai.qtip.mqtt;

import java.net.URI;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.akamai.qtip.jwt.IECJWTBuilder;

public class ClientFactory {
	public static MqttClient getIECClient(URI broker, String clientId, String[] authGroups) throws Exception {
		MqttClient client = new MqttClient(broker.toString(), clientId, new MemoryPersistence());

        MqttConnectOptions connOpts = new MqttConnectOptions();

        // build the JWT token based on a private RSA key provided through an environment var
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		jwtBuilder.setClientId(clientId)
			.setAuthGroups(authGroups)
			.setSigningKey(System.getenv("JWT_SIGNING_KEY"));

		// update the connection with credentials
        connOpts.setUserName(clientId); // this is ignored by IEC, we provide the clientId arbitrarily
        connOpts.setPassword(jwtBuilder.build().toCharArray());

        // extract hostname from broker URL and create socket factory        
        SSLSocketFactory ssf = SocketFactory.getSNISocketFactory(broker.getHost());
        connOpts.setSocketFactory(ssf);
        System.out.println("Connecting to broker: "+broker);

        // FIXME: check the implications
        connOpts.setCleanSession(true);

        client.connect(connOpts);
		
		return client;
	}
}
