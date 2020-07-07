package com.dzenan;

import java.util.*;
import java.time.Duration;
import java.time.Instant;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TestConsumer {

	public static void calculate_thoughput(float timing, int n_messages, int msg_size) {
		System.out.println(String.format("Processed %d messsages in %f seconds", n_messages, timing));
		
		System.out.println(String.format("%f = %s", (msg_size * n_messages) / timing / (1024*1024), "MB/s"));
		System.out.println(String.format("%f = %s", n_messages / timing, "Msgs/s"));
	}
	
	public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "test-group");
        properties.put("auto.offset.reset", "earliest");


        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        List<String> topics = new ArrayList<String>();
        topics.add("javatest");
        kafkaConsumer.subscribe(topics);
        try{
                Instant start = Instant.now();
                ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
                for (ConsumerRecord<String, String> record: records){
                    System.out.println(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
                }

            Instant end = Instant.now();
            Duration timeElapsed = Duration.between(start, end);
            System.out.println("Time taken: "+ timeElapsed.toMillis()/1000 +" seconds");
            calculate_thoughput(timeElapsed.toMillis()/1000, 1000000, 100);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            kafkaConsumer.close();
        }
	}

}
