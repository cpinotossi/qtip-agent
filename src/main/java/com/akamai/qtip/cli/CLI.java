package com.akamai.qtip.cli;

import com.akamai.qtip.cli.commands.CommandClientMQTT;
import com.akamai.qtip.cli.commands.Commands;
import com.akamai.qtip.cli.commands.iec.CommandManageNamespaceConfiguration;
import com.akamai.qtip.cli.commands.iec.CommandManageNamespaceConfigurationVersion;
import com.akamai.qtip.cli.commands.iec.CommandManageReservedNamespace;
import com.akamai.qtip.cli.commands.services.iec.MQTTService;
import com.akamai.qtip.cli.commands.services.iec.NamespaceService;
import com.beust.jcommander.JCommander;
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
		NamespaceService ns = null;
		byte var10 = -1;
		switch (currentCmd.hashCode()) {
			case 109374 :
				if (currentCmd.equals("nsc")) {
					var10 = 2;
				}
				break;
			case 113079 :
				if (currentCmd.equals("rns")) {
					var10 = 1;
				}
				break;
			case 3359524 :
				if (currentCmd.equals("mqtt")) {
					var10 = 0;
				}
				break;
			case 3390712 :
				if (currentCmd.equals("nscv")) {
					var10 = 3;
				}
		}

		String var13;
		byte var14;
		switch (var10) {
			case 0 :
				MQTTService iecClient = new MQTTService();
				URI uri = new URI(String.format("ssl://%s:8883", commandClientMQTT.domain));
				if (commandClientMQTT.publish) {
					iecClient.publish(commandClientMQTT.clientid, commandClientMQTT.authgroup, commandClientMQTT.topic,
							commandClientMQTT.message, commandClientMQTT.key, commandClientMQTT.repeat, uri);
				} else {
					iecClient.subscribe(commandClientMQTT.clientid, commandClientMQTT.authgroup,
							commandClientMQTT.topic, commandClientMQTT.key, uri);
				}
				break;
			case 1 :
				ns = new NamespaceService(commandReservedNamespace.section, commandReservedNamespace.edgerc);
				var13 = commandReservedNamespace.type;
				var14 = -1;
				switch (var13.hashCode()) {
					case 3322014 :
						if (var13.equals("list")) {
							var14 = 0;
						}
						break;
					case 1097075900 :
						if (var13.equals("reserve")) {
							var14 = 1;
						}
						break;
					case 1344479218 :
						if (var13.equals("list-all")) {
							var14 = 2;
						}
				}

				switch (var14) {
					case 0 :
						System.out.println(ns.getListReservedNamespaces(Integer.toString(commandReservedNamespace.size),
								Integer.toString(commandReservedNamespace.page)));
						System.exit(0);
						return;
					case 1 :
						System.out.println(ns.postReserveNamespace(commandReservedNamespace.requestBody));
						System.exit(0);
						return;
					case 2 :
						System.out.println(ns.getListAllReservedNamespaces(
								Boolean.toString(commandReservedNamespace.global), commandReservedNamespace.match,
								Boolean.toString(commandReservedNamespace.detail)));
						System.exit(0);
						return;
					default :
						return;
				}
			case 2 :
				ns = new NamespaceService(commandNamespaceConfiguration.namespace,
						commandNamespaceConfiguration.edgerc);
				var13 = commandNamespaceConfiguration.type;
				var14 = -1;
				switch (var13.hashCode()) {
					case -1352294148 :
						if (var13.equals("create")) {
							var14 = 1;
						}
						break;
					case -1335458389 :
						if (var13.equals("delete")) {
							var14 = 4;
						}
						break;
					case -838846263 :
						if (var13.equals("update")) {
							var14 = 3;
						}
						break;
					case 102230 :
						if (var13.equals("get")) {
							var14 = 2;
						}
						break;
					case 1344479218 :
						if (var13.equals("list-all")) {
							var14 = 0;
						}
				}

				switch (var14) {
					case 0 :
						System.out.println(
								ns.getListAllNamespaceConfigurations(commandNamespaceConfigurationVersion.namespace));
						System.exit(0);
						return;
					case 1 :
						System.out.println(
								ns.postCreateNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
										commandNamespaceConfigurationVersion.requestBody));
						System.exit(0);
						return;
					case 2 :
						System.out.println(ns.getNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction));
						System.exit(0);
						return;
					case 3 :
						System.out.println(
								ns.putUpdateNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
										commandNamespaceConfigurationVersion.jurisdiction,
										commandNamespaceConfigurationVersion.requestBody));
						System.exit(0);
						return;
					case 4 :
						System.out
								.println(ns.deleteNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
										commandNamespaceConfigurationVersion.jurisdiction));
						System.exit(0);
						return;
					default :
						return;
				}
			case 3 :
				ns = new NamespaceService(commandNamespaceConfigurationVersion.namespace,
						commandNamespaceConfigurationVersion.edgerc);
				var13 = commandNamespaceConfigurationVersion.type;
				var14 = -1;
				switch (var13.hashCode()) {
					case -1996763020 :
						if (var13.equals("deactivate")) {
							var14 = 2;
						}
						break;
					case -1655974669 :
						if (var13.equals("activate")) {
							var14 = 3;
						}
						break;
					case -1352294148 :
						if (var13.equals("create")) {
							var14 = 1;
						}
						break;
					case 3322014 :
						if (var13.equals("list")) {
							var14 = 0;
						}
						break;
					case 1515627643 :
						if (var13.equals("list-operations")) {
							var14 = 4;
						}
				}

				switch (var14) {
					case 0 :
						System.out.println(
								ns.getVersionsNamespaceConfiguration(commandNamespaceConfigurationVersion.namespace,
										commandNamespaceConfigurationVersion.jurisdiction));
						System.exit(0);
						return;
					case 1 :
						System.out.println(ns.postCreateVersionOfNamespaceConfiguration(
								commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction,
								commandNamespaceConfigurationVersion.requestBody));
						System.exit(0);
						return;
					case 2 :
						System.out.println(ns.putDeactivateVersionOfNamespaceConfiguration(
								commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction,
								Integer.toString(commandNamespaceConfigurationVersion.activationState)));
						System.exit(0);
						return;
					case 3 :
						System.out.println(ns.putActivateVersionOfNamespaceConfiguration(
								commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction,
								commandNamespaceConfigurationVersion.version,
								Integer.toString(commandNamespaceConfigurationVersion.activationState)));
						System.exit(0);
						return;
					case 4 :
						System.out.println(ns.getListAllOperationsForConfigurationVersions(
								commandNamespaceConfigurationVersion.namespace,
								commandNamespaceConfigurationVersion.jurisdiction));
						System.exit(0);
						return;
					default :
						return;
				}
			default :
				System.out.println("please use a command:");
		}

	}
}