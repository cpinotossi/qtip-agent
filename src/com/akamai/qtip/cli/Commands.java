package com.akamai.qtip.cli;

import com.beust.jcommander.Parameter;

public class Commands{

	@Parameter(names = { "--verbose", "-v" }, description = "Make the operation more talkative", required = false, hidden = true)
	public boolean verbose = false;

    @Parameter(names = {"--message","-m"}, description = "Hostname (Property) referenced inside an akamai configuration.", required = false)
	public String message;
    
    @Parameter(names = {"--help"}, help = true)
    public boolean help = false;
    
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

