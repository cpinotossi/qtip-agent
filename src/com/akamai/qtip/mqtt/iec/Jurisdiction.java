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
	US {
		@Override
		public String toString() {
			return "us";
		}
	};

	static public Jurisdiction getDefault() {
		return EU;
	}
}
