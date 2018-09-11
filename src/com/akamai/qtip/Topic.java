package com.akamai.qtip;

public enum Topic {
	BEACONS {
		@Override
		public String toString() { return "measures"; }
	},
	CHATTER {
		@Override
		public String toString() { return "chatter"; }
	}
}
