package com.akamai.qtip.cli.commands.iec;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Akamai IoT Edge Connect: Manage Akamai Namespace")
public class CommandManageNamespaceConfiguration {
	@Parameter(names = {
			"--edgerc"}, description = "Full path to the credentials file. Default: <user.home>/.edgerc", required = false)
	public String edgerc = "~/.edgerc";
	@Parameter(names = {
			"--section"}, description = "API Client Name inside the edgerc. Default: api-client", required = false)
	public String section = "default";
	@Parameter(names = {"--type", "-t"}, description = "Mode (list-all|create|get|update|delete)", required = true)
	public String type = "list";
	@Parameter(names = {"--request-body", "-r"}, description = "JSON Configuration to be uploaded", required = false)
	public String requestBody;
	@Parameter(names = {"--namespace", "-n"}, description = "The name of a namespace.", required = false)
	public String namespace;
	@Parameter(names = {"--jurisdiction",
			"-j"}, description = "The name of a jurisdiction. The following options are available:na for North America, eu for Europe, jp for Japan, cn for China, sk for South Korea, br for Brazil, or xw for the rest of the world.", required = false)
	public String jurisdiction = "";
}