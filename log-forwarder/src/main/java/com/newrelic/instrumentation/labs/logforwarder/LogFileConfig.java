package com.newrelic.instrumentation.labs.logforwarder;

public class LogFileConfig {
	private final String name;
	private final String filePath;
	private final String logtype;

	public LogFileConfig(String name, String filePath, String logtype) {
		this.name = name;
		this.filePath = filePath;
		this.logtype = logtype;
	}

	public String getName() {
		return name;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getLogType() {
		return logtype;
	}
}
