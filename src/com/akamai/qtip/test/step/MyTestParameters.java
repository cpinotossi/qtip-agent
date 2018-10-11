package com.akamai.qtip.test.step;

import com.akamai.qtip.mqtt.iec.Jurisdiction;

import lombok.Getter;
import lombok.Setter;

public class MyTestParameters {
	
	@Getter @Setter private boolean result=false;
	@Getter @Setter private boolean isDebug=false;
	@Getter @Setter private String agentID = "agent-paris";
	@Getter @Setter private String[] agentAuthGroup = {"agent:pub","agent:sub"};
	@Getter @Setter private String clientID = "client-paris";
	@Getter @Setter private String[] clientAuthGroup = {"client:pub","client:sub"};
	@Getter @Setter private String hostname = "qtip.us.a2s.ninja";
	@Getter @Setter private Jurisdiction jurisdiction = Jurisdiction.US;
	@Getter @Setter private String topicHelloAll = "helloall";
	@Getter @Setter private String topicHelloYou = "helloyou/"+clientID;
	@Getter @Setter private String topicTest = "test/"+agentID;
	@Getter @Setter private String topicResult = "result/"+clientID;
	@Getter @Setter private String topicResultStore = "resultstore/";
	@Getter @Setter private String uploadPathRoot = "/data";
	@Getter @Setter private String keyPrivate = System.getenv("JWT_SIGNING_KEY");
	@Getter @Setter private String clientHelloAllMessage = clientID;
	@Getter @Setter private String agentHelloYouMessage = agentID;
	@Getter @Setter private String clientTestMessage = clientID;
	@Getter @Setter private String clientResultMessage = "This is the result of your test";
	@Getter @Setter private String uploadBody = "This is content delivered via POST body";
}
