package com.akamai.qtip;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttClient;

import com.akamai.qtip.jwt.IECJWTBuilder;
import com.akamai.qtip.mqtt.iec.ClientBuilder;
import com.akamai.qtip.mqtt.iec.Jurisdiction;

/**
 * Facade class with IEC related utily functions.
 *
 * @author ahogg
 */
public class IEC {

	static public String jwt(String clientId, String[] authGroups) throws IOException {
		// build the JWT token based on a private RSA key provided through an
		// environment var
		IECJWTBuilder jwtBuilder = new IECJWTBuilder();
		jwtBuilder.setClientId(clientId).setAuthGroups(authGroups).setSigningKey(System.getenv("JWT_SIGNING_KEY"));
		return jwtBuilder.build();
	}

	static public MqttClient mqttClient(Jurisdiction jurisdiction, String[] authGroups) throws Exception {
		ClientBuilder clientBuilder = new ClientBuilder();
		String jwt = jwt(MqttClient.generateClientId(), authGroups);
		clientBuilder.setBrokerURI(Broker.getURI(jurisdiction)).addAuthGroups(authGroups)
				.setJWT(jwt);
		return clientBuilder.build();
	}
}
