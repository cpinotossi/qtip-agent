package com.akamai.qtip.cli.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Subscribe and Publish via MQTT Protocol")
public class CommandClientMQTT {

    @Parameter(names = {"--message","-m"}, description = "Hostname (Property) referenced inside an akamai configuration.", required = false)
	public String message;
        
    @Parameter(names = {"--publish","-p"}, description = "By default we will subscribe, if you like to publish change the value to true")
    public boolean publish = false;
    
	@Parameter(names = { "--clientid","-c"}, description = "API Client Name inside the edgerc. Default: api-client", required = true)
	public String clientid;

	@Parameter(names = { "--authgroup","-a"}, description = "API Client Name inside the edgerc. Default: api-client", required = true)
	public String authgroup;
	
    @Parameter(names = {"--topic","-t"}, description = "MQTT Topic", required = true)
	public String topic;
    
    @Parameter(names = {"--key","-k"}, description = "Path to the key file", required = true)
	public String key;
}
