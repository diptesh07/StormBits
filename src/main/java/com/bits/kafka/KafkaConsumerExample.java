package com.bits.kafka;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaConsumerExample {

	private final static String TOPIC = "TrialTopic";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

	private static Consumer<Long, String> getConsumer() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerExample");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		final Consumer<Long, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(TOPIC));
		return consumer;
	}

	private static void runConsumer() throws InterruptedException {

		final Consumer<Long, String> consumer = getConsumer();
		int noRecordCount = 0;

		while (true) {
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

			if (consumerRecords.count() == 0) {
				System.out.println("No records");
				noRecordCount++;
			}

			if (noRecordCount > 10000)
				break;

			consumerRecords.forEach(record -> {
				System.out.println("Consumer Record = " + record.key() + " " + record.value());
			});
			consumer.commitAsync();
		}
		consumer.close();
	}

	public static void main(String args[]) {
		try {
			runConsumer();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
