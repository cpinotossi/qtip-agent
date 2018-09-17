package com.akamai.qtip.test;

import com.akamai.qtip.Agent;

public class TestAgent {

	public static void main(String[] args) throws Exception {
		Agent agent = new Agent();
		agent.run();
		Thread.sleep(2000);
	}
}