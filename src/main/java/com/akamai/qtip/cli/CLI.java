package com.akamai.qtip.cli;

import com.akamai.qtip.Broker;
import com.akamai.qtip.cli.commands.CommandClientMQTT;
import com.akamai.qtip.cli.commands.Commands;
import com.akamai.qtip.cli.commands.iec.CommandManageNamespaceConfiguration;
import com.akamai.qtip.cli.commands.iec.CommandManageNamespaceConfigurationVersion;
import com.akamai.qtip.cli.commands.iec.CommandManageReservedNamespace;
import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URI;

public class CLI {
	public static void main(String[] args) throws Exception {
		if (null == args || args.length == 0) {
			System.out.println("try '--help' or '-h' for more information");
			System.exit(1);
		}

		Commands commands = new Commands();
		CommandClientMQTT commandClientMQTT = new CommandClientMQTT();
		CommandManageNamespaceConfiguration commandNamespaceConfiguration = new CommandManageNamespaceConfiguration();
		CommandManageNamespaceConfigurationVersion commandNamespaceConfigurationVersion = new CommandManageNamespaceConfigurationVersion();
		CommandManageReservedNamespace commandReservedNamespace = new CommandManageReservedNamespace();
		JCommander jc = new JCommander();
		jc.addObject(commands);
		jc.addCommand("mqtt", commandClientMQTT);
		jc.addCommand("rns", commandReservedNamespace);
		jc.addCommand("nscv", commandNamespaceConfigurationVersion);
		jc.addCommand("nsc", commandNamespaceConfiguration);

		try {
			jc.parse(args);
		} catch (Exception var15) {
			System.out.println(var15.getMessage());
			jc.usage();
			System.exit(1);
		}

		String currentCmd = jc.getParsedCommand();
		com.akamai.qtip.services.iec.NamespaceService ns = null;

		switch (currentCmd) {
		case "mqtt":
			com.akamai.qtip.services.iec.MQTTService iecClient = new com.akamai.qtip.services.iec.MQTTService();
			URI uri = null;
			uri = Broker.getURITLS(commandClientMQTT.domain);
			if (commandClientMQTT.publish) {
				iecClient.publish(commandClientMQTT.clientIdName, commandClientMQTT.authgroupName,
						commandClientMQTT.clientid, commandClientMQTT.authgroup, commandClientMQTT.topic,
						commandClientMQTT.message, commandClientMQTT.key, commandClientMQTT.repeat, uri);
			} else {
				iecClient.subscribe(commandClientMQTT.clientIdName, commandClientMQTT.authgroupName,
						commandClientMQTT.clientid, commandClientMQTT.authgroup, commandClientMQTT.topic,
						commandClientMQTT.key, uri);
			}
			break;
		case "rns":
			ns = new com.akamai.qtip.services.iec.NamespaceService(commandReservedNamespace.section,
					commandReservedNamespace.edgerc);
			switch (commandReservedNamespace.type) {
			case "list":
				System.out.println(
						jsonPrettyPrint(ns.getListReservedNamespaces(Integer.toString(commandReservedNamespace.size),
								Integer.toString(commandReservedNamespace.page))));
				System.exit(0);
				return;
			case "reserve":
				System.out.println(jsonPrettyPrint(ns.postReserveNamespace(commandReservedNamespace.requestBody)));
				System.exit(0);
				return;
			case "list-all":
				System.out.println(jsonPrettyPrint(
						ns.getListAllReservedNamespaces(Boolean.toString(commandReservedNamespace.global),
								commandReservedNamespace.match, Boolean.toString(commandReservedNamespace.detail))));
				System.exit(0);
				return;
			default:
				return;
			}
		case "nsc":
			ns = new com.akamai.qtip.services.iec.NamespaceService(commandNamespaceConfiguration.section,
					commandNamespaceConfiguration.edgerc);
			switch (commandNamespaceConfiguration.type) {
			case "list-all":
				System.out.println(
						jsonPrettyPrint(ns.getListAllNamespaceConfigurations(commandNamespaceConfiguration.namespace)));
				System.exit(0);
				return;
			case "create":
				System.out.println(jsonPrettyPrint(ns.postCreateNamespaceConfiguration(
						commandNamespaceConfiguration.namespace, commandNamespaceConfiguration.requestBody)));
				System.exit(0);
				return;
			case "get":
				System.out.println(jsonPrettyPrint(ns.getNamespaceConfiguration(commandNamespaceConfiguration.namespace,
						commandNamespaceConfiguration.jurisdiction)));
				System.exit(0);
				return;
			case "update":
				System.out.println(jsonPrettyPrint(ns.putUpdateNamespaceConfiguration(
						commandNamespaceConfiguration.namespace, commandNamespaceConfiguration.jurisdiction,
						commandNamespaceConfiguration.requestBody)));
				System.exit(0);
				return;
			case "delete":
				System.out.println(jsonPrettyPrint(ns.deleteNamespaceConfiguration(
						commandNamespaceConfiguration.namespace, commandNamespaceConfiguration.jurisdiction)));
				System.exit(0);
				return;
			default:
				return;
			}
		case "nscv":
			ns = new com.akamai.qtip.services.iec.NamespaceService(commandNamespaceConfigurationVersion.section,
					commandNamespaceConfigurationVersion.edgerc);

			switch (commandNamespaceConfigurationVersion.type) {
			case "list":
				System.out.println(jsonPrettyPrint(
						ns.getVersionsNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction)));
				System.exit(0);
				return;
			case "create":
				System.out.println(jsonPrettyPrint(
						ns.postCreateVersionOfNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction,
								commandNamespaceConfigurationVersion.requestBody)));
				System.exit(0);
				return;
			case "deactivate":
				System.out.println(jsonPrettyPrint(
						ns.putDeactivateVersionOfNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction,
								Integer.toString(commandNamespaceConfigurationVersion.activationState))));
				System.exit(0);
				return;
			case "activate":
				System.out.println(jsonPrettyPrint(ns.putActivateVersionOfNamespaceConfiguration(
						commandNamespaceConfigurationVersion.namespace,
						commandNamespaceConfigurationVersion.jurisdiction, commandNamespaceConfigurationVersion.version,
						Integer.toString(commandNamespaceConfigurationVersion.activationState))));
				System.exit(0);
				return;
			case "list-operations":
				System.out.println(jsonPrettyPrint(
						ns.getListAllOperationsForConfigurationVersions(commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction)));
				System.exit(0);
				return;
			default:
				return;
			}
		default:
			System.out.println("please use a command:");
		}

	}

	private static String jsonPrettyPrint(String uglyJSONString) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJSONString);
		return gson.toJson(je);
	}
}