package com.newrelic.instrumentation.labs.logforwarder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogProducer implements Runnable {
	private final List<LogFileConfig> logFiles;
	private final Map<String, Long> fileOffsets = new HashMap<>();

	public LogProducer(List<LogFileConfig> logFiles) {
		this.logFiles = logFiles;
	}

	@Override
	public void run() {
		try {
			while (true) {
				for (LogFileConfig logFile : logFiles) {
					List<String> newLines = null;
					try {
						newLines = readNewLines(logFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!newLines.isEmpty()) {
						LogForwarder.addToQueue(newLines, logFile.getName(), logFile.getLogType(),
								logFile.getFilePath());
					}
				}
				Thread.sleep(1000); // Sleep between checks (adjust as needed)
			}
		} catch (InterruptedException e) {
			System.err.println("Log producer task interrupted: " + e.getMessage());
			Thread.currentThread().interrupt(); // Preserve interrupted status
		}
	}

	private List<String> readNewLines(LogFileConfig logFile) throws IOException {
		List<String> lines = new ArrayList<>();
		long currentPosition = getFileOffset(logFile.getFilePath());

		try (RandomAccessFile raf = new RandomAccessFile(logFile.getFilePath(), "r")) {
			raf.seek(currentPosition);

			String line;
			while ((line = raf.readLine()) != null) {
				if (!line.isEmpty()) {
					lines.add(line);
				}
			}

			// Check if the file has been truncated (log rollover)
			long fileSize = raf.length();
			if (currentPosition > fileSize) {
				// Reset offset to beginning of the new file
				updateFileOffset(logFile.getFilePath(), 0L);
			} else {
				// Update file offset after reading
				updateFileOffset(logFile.getFilePath(), raf.getFilePointer());
			}
		}

		return lines;
	}

	private synchronized long getFileOffset(String filePath) {
		return fileOffsets.getOrDefault(filePath, 0L);
	}

	private synchronized void updateFileOffset(String filePath, long offset) {
		fileOffsets.put(filePath, offset);
	}
}
