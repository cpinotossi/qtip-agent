package com.akamai.qtip.cli.commands.iec;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Akamai IoT Edge Connect: Manage Akamai Reserved Namespace")
public class CommandManageNamespaceConfigurationVersion {
	@Parameter(names = {
			"--edgerc"}, description = "Full path to the credentials file. Default: <user.home>/.edgerc", required = false)
	public String edgerc = "~/.edgerc";
	@Parameter(names = {
			"--section"}, description = "API Client Name inside the edgerc. Default: api-client", required = false)
	public String section = "default";
	@Parameter(names = {"--request-body", "-r"}, description = "JSON Configuration to be uploaded", required = false)
	public String requestBody;
	@Parameter(names = {"--type",
			"-t"}, description = "Mode (list|create|deactivate|activate|list-operations)", required = true)
	public String type = "list";
	@Parameter(names = {"--namespace",
			"-n"}, description = "This operation lists all namespaces reserved for the account of the user making the request, up to 100 namespaces on a page.", required = false)
	public String namespace;
	@Parameter(names = {"--jurisdiction",
			"-j"}, description = "By default we will subscribe, if you like to publish change the value to true", required = false)
	public String jurisdiction = "";
	@Parameter(names = {"--version",
			"-v"}, description = "The configuration version that you want to activate.", required = false)
	public String version;
	@Parameter(names = {"--activation-state",
			"-a"}, description = "Specify activated as a keyword to activate the specified configuration version.", required = false)
	public int activationState = 1;
}