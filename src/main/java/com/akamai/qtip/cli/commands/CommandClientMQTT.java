package com.akamai.qtip.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Subscribe and Publish via MQTT Protocol")
public class CommandClientMQTT {
	@Parameter(names = {"--message",
			"-m"}, description = "Message", required = false)
	public String message;
	@Parameter(names = {"--publish",
			"-p"}, description = "By default we will subscribe, if you like to publish change the value to true")
	public boolean publish = false;
	@Parameter(names = {"--clientid", "-c"}, description = "MQTT Client ID", required = true)
	public String clientid;
	@Parameter(names = {"--authgroup",
			"-a"}, description = "Authorization Group used in combination with Akamai IoT Edge Connect", required = true)
	public String authgroup;
	@Parameter(names = {"--topic", "-t"}, description = "MQTT Topic", required = true)
	public String topic;
	@Parameter(names = {"--key", "-k"}, description = "Path to the key file", required = true)
	public String key;
	@Parameter(names = {"--domain", "-d"}, description = "Domain/Hostname of the mqtt server", required = true)
	public String domain;
	@Parameter(names = {"--repeat", "-r"}, description = "Number of times to repeate the message", required = false)
	public int repeat = 1;
	
	@Parameter(names = {"--clientid-name",}, description = "Name of the client id JWT claim", required = false)
	public String clientIdName="client-id";
	@Parameter(names = {"--authgroup-name",}, description = "Name of the Authorization Group JWT claim", required = false)
	public String authgroupName="auth-groups";	
	
}