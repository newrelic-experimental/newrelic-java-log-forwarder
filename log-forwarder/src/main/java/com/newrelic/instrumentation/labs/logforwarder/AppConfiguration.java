package com.newrelic.instrumentation.labs.logforwarder;

import java.util.List;

public class AppConfiguration {
	private String apiKey;
	private String applicationName;
	private String apiUrl;

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apuUrl) {
		this.apiUrl = apuUrl;
	}

	private long logCheckIntervalMs;
	private List<LogFileConfig> logFiles;

	public AppConfiguration(String apiKey, String applicationName, long logCheckIntervalMs,
			List<LogFileConfig> logFiles, String apiUrl) {
		this.apiKey = apiKey;
		this.applicationName = applicationName;
		this.logCheckIntervalMs = logCheckIntervalMs;
		this.logFiles = logFiles;
		this.apiUrl = apiUrl;
	}

	// Getters for apiKey, applicationName, logCheckIntervalMs, and logFiles
	public String getApiKey() {
		return apiKey;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public long getLogCheckIntervalMs() {
		return logCheckIntervalMs;
	}

	public List<LogFileConfig> getLogFiles() {
		return logFiles;
	}
}
