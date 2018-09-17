package com.akamai.qtip;

public enum Topic {
	BEACONS {
		@Override
		public String toString() { return "measures"; }
	},
	CHATTER {
		@Override
		public String toString() { return "chatter"; }
	},
	CHATTER_START_TEST {
		@Override
		public String toString() { return "chatter/start"; }
	},
	CHATTER_ABORT_TEST {
		@Override
		public String toString() { return "chatter/abort"; }
	},
	BEACONS_PUBLISHERS {
		@Override
		public String toString() { return "measures/publishers"; }
	},
	BEACONS_SUBSCRIBERS {
		@Override
		public String toString() { return "measures/subscribers"; }
	}
}
