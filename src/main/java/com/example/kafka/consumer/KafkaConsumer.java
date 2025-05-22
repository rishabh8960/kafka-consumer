package com.example.kafka.consumer;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * KafkaConsumer class to consume messages from Kafka, aggregate them, and write to files.
 */
@Component
public class KafkaConsumer extends RouteBuilder {

    @Value("${aggregation.size}") // Configurable aggregation size
    private int aggregationSize;

    @Value("${aggregation.timeout}") // Configurable aggregation timeout
    private int aggregationTimeout;

    @Value("${output.directory}") // Configurable output directory
    private String outputDirectory;

    private final AtomicInteger fileCounter = new AtomicInteger(0); // Counter for file names

    @Override
    public void configure() throws Exception {
        from("kafka:Uniper_Topic?brokers=localhost:9092")
                .log("Consumed message: ${body}")
                .aggregate(header("id"), new GroupedMessageAggregationStrategy())
                .completionSize(aggregationSize) // Aggregate based on the configured size
                .completionTimeout(aggregationTimeout) // Timeout to avoid indefinite waiting
                .log("Aggregated messages: ${body}")
                .process(exchange -> {
                    // Add the counter value to the file name
                    int currentCount = fileCounter.incrementAndGet();
                    exchange.getIn().setHeader("fileCounter", currentCount);
                })
                .to("file:" + outputDirectory + "?fileName=aggregated-messages-${header.id}-${header.fileCounter}.txt");
    }

    /**
     * Custom Aggregation Strategy to group messages.
     */
    private static class GroupedMessageAggregationStrategy implements AggregationStrategy {
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }
            String oldBody = oldExchange.getIn().getBody(String.class);
            String newBody = newExchange.getIn().getBody(String.class);
            oldExchange.getIn().setBody(oldBody + "\n" + newBody);
            return oldExchange;
        }
    }
}