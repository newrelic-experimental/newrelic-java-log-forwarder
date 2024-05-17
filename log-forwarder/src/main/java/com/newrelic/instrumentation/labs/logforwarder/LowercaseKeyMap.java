package com.newrelic.instrumentation.labs.logforwarder;

import java.util.HashMap;

public class LowercaseKeyMap extends HashMap<String, String> {
	@Override
	public String put(String key, String value) {
		return super.put(key.toLowerCase(), value);
	}

	// Override other relevant methods if needed to ensure keys are always handled
	// in lowercase
}
