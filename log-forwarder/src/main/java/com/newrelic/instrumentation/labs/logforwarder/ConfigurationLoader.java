package com.newrelic.instrumentation.labs.logforwarder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationLoader {
	private static final String CONFIG_ENV_VAR = "LOG_CONFIG";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static AppConfiguration loadConfiguration() throws IOException {
		String configFile = System.getenv(CONFIG_ENV_VAR);

		if (configFile == null || configFile.isEmpty()) {
			throw new IllegalStateException("Environment variable " + CONFIG_ENV_VAR + " is not set.\n");
		}

		JsonNode configNode = objectMapper.readTree(new File(configFile));

		String apiKey = configNode.get("api_key").asText();
		String applicationName = configNode.get("application_name").asText();
		long logCheckIntervalMs = configNode.get("log_check_interval_ms").asLong();
		String apiURL = "https://log-api.newrelic.com/log/v1";
		apiURL = configNode.get("api_url").asText();

		List<LogFileConfig> logFiles = parseLogFiles(configNode.get("log_files"));

		return new AppConfiguration(apiKey, applicationName, logCheckIntervalMs, logFiles, apiURL);
	}

	private static List<LogFileConfig> parseLogFiles(JsonNode logFilesNode) {
		List<LogFileConfig> logFileConfigs = new ArrayList<>();
		for (JsonNode fileNode : logFilesNode) {
			String name = fileNode.get("name").asText();
			String filePath = fileNode.get("file").asText();
			String logType = fileNode.get("attributes").get("logtype").asText(); // Read logtype attribute

			logFileConfigs.add(new LogFileConfig(name, filePath, logType));
		}
		return logFileConfigs;
	}
}
