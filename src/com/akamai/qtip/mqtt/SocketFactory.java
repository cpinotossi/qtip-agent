package com.akamai.qtip.mqtt;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * MQTT brokers can be connected to over TCP or TLS. This class exposes different factories
 * allowing us to implement different connection models.  
 * 
 * @author ahogg
 */
public class SocketFactory {

	/**
	 * Taken from the MqttIECTest.java class provided by Sharath Rao on Aloha:
	 * 
	 * https://ac.akamai.com/docs/DOC-51625
	 * 
	 * @param hostName
	 * @return SSLSocketFactory
	 * @throws Exception
	 */
	public static SSLSocketFactory getSNISocketFactory(String hostName) throws Exception {
		SSLContext context = SSLContext.getInstance("TLSv1");

		TrustManager[] trustAllCerts = new TrustManager[] { (TrustManager) new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		context.init(null, trustAllCerts, null);

		SSLSocketFactory factory = context.getSocketFactory();
		// to manually switch on SNI
		factory = new SNIEnabledSSLSocketFactory(factory, hostName);
		return factory;
	}
}
