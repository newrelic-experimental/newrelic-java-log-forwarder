<a href="https://opensource.newrelic.com/oss-category/#new-relic-experimental"><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/dark/Experimental.png"><source media="(prefers-color-scheme: light)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"><img alt="New Relic Open Source experimental project banner." src="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"></picture></a>


![GitHub forks](https://img.shields.io/github/forks/newrelic-experimental/java-instrumentation-template?style=social)
![GitHub stars](https://img.shields.io/github/stars/newrelic-experimental/java-instrumentation-template?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/newrelic-experimental/java-instrumentation-template?style=social)

![GitHub all releases](https://img.shields.io/github/downloads/newrelic-experimental/java-instrumentation-template/total)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/newrelic-experimental/java-instrumentation-template)
![GitHub last commit](https://img.shields.io/github/last-commit/newrelic-experimental/java-instrumentation-template)
![GitHub Release Date](https://img.shields.io/github/release-date/newrelic-experimental/java-instrumentation-template)


![GitHub issues](https://img.shields.io/github/issues/newrelic-experimental/java-instrumentation-template)
![GitHub issues closed](https://img.shields.io/github/issues-closed/newrelic-experimental/java-instrumentation-template)
![GitHub pull requests](https://img.shields.io/github/issues-pr/newrelic-experimental/java-instrumentation-template)
![GitHub pull requests closed](https://img.shields.io/github/issues-pr-closed/newrelic-experimental/java-instrumentation-template)


# New Relic Java Log Forwarder
Log Forwarder for AIX, HP-UX, Linux, OSX/MacOS & Solaris/SunOS

This Java log forwarder plugin proves invaluable in scenarios where out-of-the-box New Relic logging solutions are absent for specific platforms, such as AIX, HP-UX, Solaris, OSX/MacOS, and Linux operating systems. It seamlessly integrates into environments running JDK 8 (& above) , providing a straightforward configuration process and effortless execution. Whether encountering unique system configurations or exploring diverse platforms, this plugin stands as a reliable solution for forwarding logs to New Relic, ensuring comprehensive observability across a broad spectrum of systems.


# Installation Guide

## Step 1: Download and Extract the Release
Download the release and extract the JAR file `log-forwarder-all.jar`.

## Step 2: Create a Configuration File `config.json`
```json
{
  "api_key": "<ingest_key>",
  "application_name": "nrlabs-java-loggerv1.0",
  "log_check_interval_ms": 120000,
  "api_url": "https://log-api.newrelic.com/log/v1",
  "log_files": [
    {
      "name": "mylog-file",
      "file": "/Users/gsidhwani/Documents/GitHub/NR-LOG/newrelic-java-logger/mylog.log",
      "attributes": {
        "logtype": "custom"
      }
    },
    {
      "name": "mynginx-logs",
      "file": "/Users/gsidhwani/Documents/GitHub/NR-LOG/newrelic-java-logger/xyz.log",
      "attributes": {
        "logtype": "nginx"
      }
    }
  ]
}
```
- api_key: The API key used for authentication when sending logs to New Relic.
- application_name: The name of the application or service from which the logs are being collected.
- log_check_interval_ms: The interval, in milliseconds, at which the log forwarder checks for new log entries to send to New Relic.
- api_url: The URL of the [New Relic log API endpoint](https://docs.newrelic.com/docs/logs/log-api/introduction-log-api/) where the logs will be sent.
- log_files: An array of log files to be monitored and forwarded.
- name: A descriptive name for the log file.
- file: The absolute path to the log file on the file system.
- attributes: Additional attributes or metadata associated with the log file, such as the [logtype](https://docs.newrelic.com/docs/logs/ui-data/parsing/#grok).

These parameters allow you to configure the log forwarder to monitor specific log files, collect log entries at regular intervals, and send them to New Relic for analysis and visualization. Adjust these settings according to your application's requirements and environment.

## Step 3: Set Environment Variable `LOG_CONFIG`
```bash
export LOG_CONFIG=config.json  
```

## Step 4: Run the Log Forwarder
```bash
java -jar log-forwarder-all.jar
```

Follow these steps to install and configure the log forwarder for your Java application.

## Building
```bash
./gradlew clean shadowJar
```
The jar log-forwarder/build/libs/log-forwarder-all.jar will be created.

## Logs at New Relic Platform - A Sample

<img width="1910" alt="image" src="https://github.com/newrelic-experimental/newrelic-java-log-forwarder/assets/113113837/6a482aec-9ef8-42bf-a443-627b82498f20">


## Support

New Relic has open-sourced this project. This project is provided AS-IS WITHOUT WARRANTY OR DEDICATED SUPPORT. Issues and contributions should be reported to the project here on GitHub.

>We encourage you to bring your experiences and questions to the [Explorers Hub](https://discuss.newrelic.com) where our community members collaborate on solutions and new ideas.

## Contributing

We encourage your contributions to improve Salesforce Commerce Cloud for New Relic Browser! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project. If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company, please drop us an email at opensource@newrelic.com.

**A note about vulnerabilities**

As noted in our [security policy](../../security/policy), New Relic is committed to the privacy and security of our customers and their data. We believe that providing coordinated disclosure by security researchers and engaging with the security community are important means to achieve our security goals.

If you believe you have found a security vulnerability in this project or any of New Relic's products or websites, we welcome and greatly appreciate you reporting it to New Relic through [HackerOne](https://hackerone.com/newrelic).

## License

New Relic Java Log Forwarder is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.

