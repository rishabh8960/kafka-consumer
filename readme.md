# Kafka Consumer POC

## Overview

-This project is a Proof of Concept (POC) demonstrating the use of Apache Camel with Kafka to consume, aggregate, and process messages. The application reads messages from a Kafka topic, groups them by a specific ID, aggregates them based on a configurable size, and writes the aggregated messages to files.

## Features

**Kafka Integration**: Consumes messages from a Kafka topic using the Camel Kafka component.

**Message Aggregation**: Groups and aggregates messages by a specific header (id).

**Configurable Parameters:**

 - Number of messages to aggregate (aggregation.size).
 - Timeout for aggregation (aggregation.timeout).
 - Output directory for files (output.directory).

**File Writing:** Writes aggregated messages to files with unique names.

## Technologies Used

 - Java
 - Spring Boot
 - Apache Camel
 - Apache Kafka
 - Gradle