package com.dzenan;

import java.util.*;
import java.time.Duration;
import java.time.Instant;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class TestProducer {

	public static void calculate_thoughput(float timing, int n_messages, int msg_size) {
		System.out.println(String.format("Processed %d messsages in %f seconds", n_messages, timing));
		
		System.out.println(String.format("%f = %s", (msg_size * n_messages) / timing / (1024*1024), "MB/s"));
		System.out.println(String.format("%f = %s", n_messages / timing, "Msgs/s"));
	}
	
	public static void main(String[] args) {
        //long events = Long.parseLong(args[0]);
        //Random rnd = new Random();
		
		int msg_count = 1000000;
		int msg_size = 100;
		String msg_payload = "kafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafkakafka";
		
        Properties props = new Properties();
        props.put("metadata.broker.list", "localhost:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
 
        ProducerConfig config = new ProducerConfig(props);
 
        Producer<String, String> producer = new Producer<String, String>(config);
        
        Instant start = Instant.now();
        for (int i = 0; i < msg_count; i++) { 
        	String key = "key" + i;
               KeyedMessage<String, String> data = new KeyedMessage<String, String>("javatest", key, msg_payload);
               producer.send(data);
        }
        Instant end = Instant.now();

        producer.close();
        
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("Time taken: "+ timeElapsed.toMillis()/1000 +" seconds");
        calculate_thoughput(timeElapsed.toMillis()/1000, 1000000, 100);

	}

}
