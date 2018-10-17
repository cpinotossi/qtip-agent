package com.akamai.qtip.mqtt.iec;

public enum Jurisdiction {
	EU {
		@Override
		public String toString() {
			return "eu";
		}
	},
	JP {
		@Override
		public String toString() {
			return "jp";
		}
	},
	NA {
		@Override
		public String toString() {
			return "na";
		}
	};

	static public Jurisdiction getDefault() {
		return EU;
	}
}
