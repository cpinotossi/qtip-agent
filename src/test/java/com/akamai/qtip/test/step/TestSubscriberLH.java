package com.akamai.qtip.test.step;

import java.io.IOException;
import java.net.URI;

import javax.net.ssl.SSLSocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.mqtt.SocketFactory;


public class TestSubscriberLH {

	public static void main(String[] args) throws Exception {

		String clientId ="client666";
		URI uri = new URI("ssl://iot.lufthansa.com:8883");

		String topic = "notification";
		String jwt ="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkhhcnNoYSIsImNsaWVudElEIjoiY2xpZW50NjY2IiwiYXV0aEdyb3VwcyI6Im5vdGlmaWNhdGlvblB1Yjtub3RpZmljYXRpb25TdWIifQ.ouLOBekAnH0vfxPYHgnbOJpmgdgTJPYR7PeOg6PvLASTqnvOil32p0j7GoZFz6jvHejZgmRAVuRamArnuW1bSFGmdo2ndfVUTKQ46hYM3BZ_JfLyd-IemT1qGu_TNc348C8_GFzwbQMNvBOD54B_pxST_NA5wRYoF7K2BVY_USrlZ61PKrN6ZG4-k5tF-VJDbV1VN49QRJZKtj4_OUI5w3thvammzxQBMXnJt3Ij5Ao7TahLGP47izAym_o6SP6rMLDQdJ22Ot-J61WlOos29as2S4EUEkAA1Jv0EqBkDu3rxtOyu-EIFOt648SjxTedfrmk5-TOtP5zk5AvBNHZEUg";
				
		//callback functions defined, when certain MQTT events take place
        MqttCallback callback = new MqttCallback() {
        	@Override
        	public void messageArrived(String topic, MqttMessage message) throws Exception {
        		System.out.println("MESSAGE FROM "+topic+": " +
	            new String(message.getPayload())+" with this qos: " +
	            Integer.toString(message.getQos()));
        	}

        	@Override
        	public void deliveryComplete(IMqttDeliveryToken token) { }

        	@Override
        	public void connectionLost(Throwable cause) {
        				System.out.println("Exception "+cause);
        				System.exit(0);
        	}
        };

        MqttClient client = new MqttClient(uri.toString(), clientId);
        client.setCallback(callback);
        
		// update the connection with credentials
		MqttConnectOptions connectOptions = new MqttConnectOptions();
		connectOptions.setUserName(clientId); // this is ignored by IEC, we provide the clientId arbitrarily
		connectOptions.setPassword(jwt.toCharArray());

		// extract hostname from broker URL and create socket factory
		SSLSocketFactory ssf = SocketFactory.getSNISocketFactory(uri.getHost());
		connectOptions.setSocketFactory(ssf);
		client.connect(connectOptions);
		System.out.println("Connected to "+uri+" with client ID "+client.getClientId());
		System.out.println("Subscribing to topic \""+topic+"\" qos ");
		client.subscribe(topic);
	   	// Continue waiting for messages until the Enter is pressed
		System.out.println("Press <Enter> to exit");
		try {
			System.in.read();
		} catch (IOException e) {
			//If we can't read we'll just exit
		}

		// Disconnect the client from the server
		client.disconnect();
		System.out.println("Disconnected");

	}	

}
