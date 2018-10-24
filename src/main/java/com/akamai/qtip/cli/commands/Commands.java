package com.akamai.qtip.cli.commands;

import com.beust.jcommander.Parameter;

public class Commands {
	@Parameter(names = {"--verbose",
			"-v"}, description = "Make the operation more talkative", required = false, hidden = true)
	public boolean verbose = false;
	@Parameter(names = {"--help"}, help = true)
	public boolean help = false;
}