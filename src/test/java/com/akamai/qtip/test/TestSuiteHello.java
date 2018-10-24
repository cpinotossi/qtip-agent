package com.akamai.qtip.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.akamai.qtip.test.step.TestAgentPublishToHelloYou;
import com.akamai.qtip.test.step.TestAgentSubscribeToHelloAll;
import com.akamai.qtip.test.step.TestClientPublishToHelloAll;
import com.akamai.qtip.test.step.TestClientSubscribeToHelloYou;

//TODO Implement test
@RunWith(Suite.class)
@SuiteClasses({
	TestAgentSubscribeToHelloAll.class,
	TestClientSubscribeToHelloYou.class,
	TestClientPublishToHelloAll.class,
	TestAgentPublishToHelloYou.class})

public class TestSuiteHello {
	
}
