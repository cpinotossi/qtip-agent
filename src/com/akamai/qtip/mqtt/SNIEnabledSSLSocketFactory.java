package com.akamai.qtip.mqtt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SNIEnabledSSLSocketFactory extends SSLSocketFactory {
	private final SSLSocketFactory factory;
	private final String sniHostname;

	public SNIEnabledSSLSocketFactory(SSLSocketFactory factory, String sniHostName) {
		this.factory = factory;
		this.sniHostname = sniHostName;
	}

	@Override
	public Socket createSocket(Socket socket, String hostname, int port, boolean autoClose) throws IOException {
		return registerSocket(factory.createSocket(socket, hostname, port, autoClose));
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return factory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return factory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket() throws IOException {
		return registerSocket(factory.createSocket());
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return registerSocket(factory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return registerSocket(factory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		return registerSocket(factory.createSocket(host, port, localHost, localPort));
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		return registerSocket(factory.createSocket(address, port, localAddress, localPort));
	}

	private Socket registerSocket(Socket socket) {
		if (sniHostname != null && (socket instanceof SSLSocket)) {
			SNIHostName serverName = new SNIHostName(sniHostname.getBytes());
			List<SNIServerName> serverNames = new ArrayList<SNIServerName>();
			serverNames.add(serverName);
			SSLSocket sslSocket = ((SSLSocket) socket);
			SSLParameters params = sslSocket.getSSLParameters();
			params.setServerNames(serverNames);
			sslSocket.setSSLParameters(params);
		}

		return socket;
	}
}