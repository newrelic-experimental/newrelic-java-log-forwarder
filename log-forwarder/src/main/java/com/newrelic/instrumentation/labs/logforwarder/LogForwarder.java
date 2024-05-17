package com.newrelic.instrumentation.labs.logforwarder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LogForwarder extends TimerTask {
	private static final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>();
	private final String apiKey;
	private final String applicationName;
	private final String apiURL;
	private final OkHttpClient client = new OkHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public LogForwarder(String apiKey, String applicationName, String apiURL) {
		this.apiKey = apiKey;
		this.applicationName = applicationName;
		this.apiURL = apiURL;
	}

	public static void addToQueue(List<String> lines, String name, String logtype, String filepath) {
		for (String line : lines) {
			logQueue.offer(new LogEntry(line, name, logtype, filepath));
		}
	}

	@Override
	public void run() {
		try {
			List<LogEntry> batch = new ArrayList<>();
			logQueue.drainTo(batch, 8000); // Drain up to 5000 log entries into batch as API limit is 1MB ( Approx.. 13k
											// lines)

			if (!batch.isEmpty()) {
				System.out.println("Sending " + batch.size() + " log events to New Relic...");
				sendLogsToNewRelic(batch);
			}
		} catch (Exception e) {
			System.err.println("Error sending logs to New Relic: " + e.getMessage());
		}
	}

	private void sendLogsToNewRelic(List<LogEntry> logEntries) {
		// String apiUrl = "https://log-api.newrelic.com/log/v1";
		// Get the local host's InetAddress object
		InetAddress localhost = null;
		try {
			localhost = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Retrieve the hostname from the InetAddress object
		String hostname = localhost.getHostName();

		MediaType mediaType = MediaType.parse("application/json");

		try {
			List<Map<String, String>> logEvents = new ArrayList<>();
			for (LogEntry entry : logEntries) {
				Map<String, String> logEvent = objectMapper.convertValue(entry, LowercaseKeyMap.class);

				logEvent.put("hostname", hostname); // Add logtype based on the entry's
				logEvents.add(logEvent);
			}

			String jsonPayload = objectMapper.writeValueAsString(logEvents);
			byte[] compressedPayload = gzipCompress(jsonPayload);

			RequestBody requestBody = RequestBody.create(compressedPayload, mediaType);
			Request request = new Request.Builder().url(apiURL).post(requestBody).addHeader("X-License-Key", apiKey)
					.addHeader("Content-Type", "application/json").addHeader("Content-Encoding", "gzip").build();

			Response response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				System.err.println("Failed to send logs to New Relic: " + response.code() + " - " + response.message());
			} else {
				LocalDateTime timestamp = LocalDateTime.now();
				System.out.println("Logs sent to New Relic successfully: " + timestamp);
			}
		} catch (IOException e) {
			System.err.println("Error during log forwarding: " + e.getMessage());
		}
	}

	private byte[] gzipCompress(String input) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GZIPOutputStream gzipOS = new GZIPOutputStream(bos);
		gzipOS.write(input.getBytes());
		gzipOS.close();
		return bos.toByteArray();
	}

	private static class LogEntry {
		private final String message;
		private final String name;
		private final String logtype;
		private final String filepath;

		public String getFilepath() {
			return filepath;
		}

		public LogEntry(String message, String name, String logtype, String filepath) {
			this.message = message;
			this.name = name;
			this.logtype = logtype;
			this.filepath = filepath;
		}

		public String getMessage() {
			return message;
		}

		public String getName() {
			return name;
		}

		public String getLogType() {
			return logtype;
		}
	}
}
