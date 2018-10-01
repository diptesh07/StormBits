package com.bits.kafka;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerExample {

	private final static String TOPIC = "TrialTopic";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

	private static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaProducerExample");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}

	public static void main(String args[]) {
		final Producer<Long, String> producer = createProducer();

		while(true) {
			Order order = new Order();
			order.setChargeAmount(Math.random()*1000);
			order.setCreditCardCode((int)(Math.random()*1000));
			order.setCreditCardExpiry(String.valueOf(System.currentTimeMillis()));
			order.setCreditCardNumber((Long.toString((long)(Math.random()*1000000))));
			order.setCustomerId(UUID.randomUUID().toString());
			order.setId(UUID.randomUUID().toString());
			

			try {
				ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, order.toString());
				RecordMetadata metadata = producer.send(record).get();
				Thread.sleep(5000);
//				System.out.println(metadata.offset());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
