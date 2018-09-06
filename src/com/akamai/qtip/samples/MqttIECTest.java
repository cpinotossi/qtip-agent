package com.akamai.qtip.samples;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.akamai.qtip.mqtt.SNIEnabledSSLSocketFactory;


// Sample client implementation provided by Sharath Rao
// https://ac.akamai.com/docs/DOC-51625
// Not for use

class MqttIECTest {


	// to trust all server certs or hosts
	private static SSLSocketFactory getSocketFactory(String hostName) throws Exception {
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
       //to manually switch on SNI
       factory = new SNIEnabledSSLSocketFactory(factory, "test01.iot.awesomeconfig.com");
       return factory;
    }

    
    public static void main(String[] args) {

        //topic to publish or subscribe
    	String topic        = "managers";
    	//message to publish
        String content      = "Message from MqttIECTest Java Paho Client  - TEST MESSAGE";
        //qos level
        int qos             = 0;
        //Broker URL with protocol and port
        String broker       = "ssl://test01.iot.awesomeconfig.com:8883";
        //Client name to be used by this Java client, the same needs to be the client id value in JWT token
        String clientId     = "client1234";
        //Dummy username to be used by this Java client, this username will be ignored by the IEC broker
        String userName     = "dummyUserName";
        //JWT token to be used as password by this Java client, goto https://jwt.io/ to see the decoded values
        String passWord     = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJKV1QuSU8iLCJzdWIiOiJJRUMgQmV0YSBKYW0iLCJuYW1lIjoiU2hhcmFvIiwiY2xpZW50SUQiOiJjbGllbnQxMjM0IiwiYXV0aEdyb3VwcyI6Im1hbmFnZXJzcHViO21hbmFnZXJzc3ViIn0.O_oVKYuFrSW4rfIeWPWPhZk98ezHiRPpzXJxmI_kyL6mawJuXFP57Y69rIrrUoO9PWOd0O8FAUNtFk7_4ytH1KPY2fpGM2Cw1Hc5BT1zZ7ib8XxMSeIkDUzN6ESJV9eH8O-pGb-G3wWAtUEDbQESN8EoKfSNyjuGXxV8Jh-BLh2sB5-RhaXRKad8WcgZaJ1IBvfRmuDdtgJaE__RgMsT9noY20JQraRC2yliZHI5j4xnnszAEdLoI9Rr7v8J7zaQxUAe1zWKWPxfGeehnMaL3t54gWNxmBA56UDUw5QMDypAKheoOeKAx2h4dri3ounvAeKtfYA6XV33HVFEJimsfQ";
  		
        try {
        	 MqttClient sampleClient = new MqttClient(broker, clientId);
       
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
            
            sampleClient.setCallback(callback);       	
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(userName);
            connOpts.setPassword(passWord.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setSocketFactory(getSocketFactory("test01.iot.awesomeconfig.com"));
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }   
}