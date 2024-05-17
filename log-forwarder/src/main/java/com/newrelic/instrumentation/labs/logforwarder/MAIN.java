package com.newrelic.instrumentation.labs.logforwarder;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MAIN {

	public static void main(String[] args) {
		try {

			// Load configuration
			AppConfiguration config = ConfigurationLoader.loadConfiguration();
			String apiKey = config.getApiKey();
			String apiURL = config.getApiUrl();
			String applicationName = config.getApplicationName();
			List<LogFileConfig> logFiles = config.getLogFiles();
			long logCheckIntervalMs = config.getLogCheckIntervalMs();

			// Start log forwarding task
			TimerTask logForwardingTask = new LogForwarder(apiKey, applicationName, apiURL);
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(logForwardingTask, 0, logCheckIntervalMs);

			// Start log producer task (reading log lines and adding to queue)
			Thread producerThread = new Thread(new LogProducer(logFiles));
			producerThread.start();
			System.out.println("Logging process started successfully...");
		} catch (IOException e) {
			System.err.println("Error loading configuration: " + e.getMessage());
			System.exit(1);
		}
	}
}
