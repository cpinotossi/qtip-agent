package com.akamai.qtip.subscribetest;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.akamai.qtip.Agent;
import com.akamai.qtip.Messages.StartTest;

public class SubscribeTestManager {
	private ConcurrentHashMap<String, SubscribeTest> runningTests = new ConcurrentHashMap<String, SubscribeTest>();
	private Agent agent;
	private static final int MAX_SUBSCRIBERS = 50;
	
	public SubscribeTestManager(Agent agent) {
		this.agent = agent;
	}

	public void startTest(StartTest parameters) throws URISyntaxException, Exception {
		if (runningTests.size() >= MAX_SUBSCRIBERS)
			return;
		SubscribeTest test = new SubscribeTest(agent, this, parameters);
		if (test == runningTests.putIfAbsent(parameters.getTestId(), test))
		{
			new Thread(test, "SubscribeTest_" + parameters.getTestId()).start();
		}
	}
	
	public void abortTest(String testId) {
		SubscribeTest test = runningTests.remove(testId);
		// check existing (duplicate message from MQTT / already finished ?)
		if (test == null)
			return; // log ?
		test.close();
	}

	void remove(String testId) {
		runningTests.remove(testId);
	}
	
}
