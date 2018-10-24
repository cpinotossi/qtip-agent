package com.akamai.qtip.publishtest;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.akamai.qtip.Agent;
import com.akamai.qtip.Messages.StartTest;

public class PublishTestManager {
	private Map<String, PublishTest> runningTests = new HashMap<String, PublishTest>();
	private Agent agent;
	
	public PublishTestManager(Agent agent) {
		this.agent = agent;
	}

	public void startTest(StartTest parameters) throws URISyntaxException, Exception {
		// check already running (duplicate message from MQTT ?)
		if (runningTests.containsKey(parameters.getTestId()))
			return; // log ?
		
		PublishTest test = new PublishTest(agent, parameters);
		runningTests.put(parameters.getTestId(), test);
		test.start();
	}
	
	public void abortTest(String testId) {
		PublishTest test = runningTests.get(testId);
		// check existing (duplicate message from MQTT ?)
		if (test == null)
			return; // log ?
		test.abort();
		runningTests.remove(testId);
	}
	
}
