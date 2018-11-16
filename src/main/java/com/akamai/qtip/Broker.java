package com.akamai.qtip;

import java.net.URI;
import java.net.URISyntaxException;

import com.akamai.qtip.mqtt.iec.Jurisdiction;

public final class Broker {

	static public URI getURITLS(Jurisdiction jurisdiction) throws URISyntaxException {
		String uri = String.format("ssl://qtip-%s.a2s.ninja:8883", jurisdiction.toString());
		return new URI(uri);
	}
	static public URI getURIWebsocket(Jurisdiction jurisdiction) throws URISyntaxException {
		String uri = String.format("wws://qtip-%s.a2s.ninja:443", jurisdiction.toString());
		return new URI(uri);
	}
	static public URI getURITLS(String hostname) throws URISyntaxException {
		String uri = String.format("ssl://%s:8883", hostname);
		return new URI(uri);
	}
	static public URI getURIWebsocket(String hostname) throws URISyntaxException {
		String uri = String.format("wws://%s:443", hostname);
		return new URI(uri);
	}
}
