package com.newrelic.instrumentation.labs.logforwarder;

import java.io.IOException;

public class ConfigurationLoaderTest {

	public static void main(String[] args) {
		try {
			// Load configuration
			AppConfiguration config = ConfigurationLoader.loadConfiguration();

			// Display loaded configuration
			System.out.println("Loaded Configuration:");
			System.out.println("API Key: " + config.getApiKey());
			System.out.println("Application Name: " + config.getApplicationName());
			System.out.println("Log Check Interval (ms): " + config.getLogCheckIntervalMs());
			System.out.println("apiURL: " + config.getApiUrl());
			System.out.println("Log Files:");

			for (LogFileConfig logFile : config.getLogFiles()) {
				System.out.println("  Name: " + logFile.getName());
				System.out.println("  File Path: " + logFile.getFilePath());
				System.out.println("  Log Type: " + logFile.getLogType());
				System.out.println();

			}
		} catch (IOException e) {
			System.err.println("Error loading configuration: " + e.getMessage());
		}
	}
}
