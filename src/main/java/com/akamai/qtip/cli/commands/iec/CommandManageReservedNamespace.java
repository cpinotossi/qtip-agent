package com.akamai.qtip.cli.commands.iec;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Akamai IoT Edge Connect: Manage Akamai Reserved Namespace")
public class CommandManageReservedNamespace {
	@Parameter(names = {
			"--edgerc"}, description = "Full path to the credentials file. Default: <user.home>/.edgerc", required = false)
	public String edgerc = "~/.edgerc";
	@Parameter(names = {
			"--section"}, description = "API Client Name inside the edgerc. Default: api-client", required = false)
	public String section = "default";
	@Parameter(names = {"--type", "-t"}, description = "Mode (list|reserve|list-all|remove)", required = true)
	public String type = "list";
	@Parameter(names = {"--request-body", "-r"}, description = "JSON Configuration to be uploaded", required = false)
	public String requestBody;
	@Parameter(names = {"--size", "-s"}, description = "The number of namespaces per page.", required = false)
	public int size = 2;
	@Parameter(names = {"--page",
			"-p"}, description = "The number of the page with the number of namespaces to list specified in size. Page numbers start at zero.", required = false)
	public int page = 50;
	@Parameter(names = {"--global",
			"-g"}, description = "Whether to search for reserved namespaces in all accounts or in the user’s account only.", required = false)
	public boolean global = true;
	@Parameter(names = {"--match",
			"-m"}, description = "A search of string matching content. You can use the wildcard character * to search for unspecified content.", required = false)
	public String match = "*";
	@Parameter(names = {"--detail",
			"-d"}, description = "Whether to show connection details for the namespaces in the user’s account.", required = false)
	public boolean detail = true;
	@Parameter(names = {"--namespace", "-n"}, description = "The name of a namespace.", required = false)
	public String namespace;
}