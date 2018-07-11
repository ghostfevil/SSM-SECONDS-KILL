package com.crossoverjie.seconds.kill.service.impl;

import com.crossoverJie.seconds.kill.api.dto.JsonSerializer;
import com.crossoverJie.seconds.kill.api.dto.Stock;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.crossoverJie.seconds.kill.config.KafkaConfig;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 07/05/2018 10:02
 * @since JDK 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-mvc.xml")
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class KafkaProductTest {
	
    @Value("${kafka.brokerList}")
    private String brokerList ;

    @Value("${kafka.swicth}")
    private boolean check ;


    @Test
    public void testProduct(){
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCount(10);
        stock.setName("手机");
        stock.setSale(0);
        stock.setVersion(0);
        System.out.println("send start");
        Map<String, Object> props = new HashMap<String, Object>(16);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", brokerList);
        props.put("bootstrap.servers", brokerList);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", JsonSerializer.class);
        KafkaProducer<String, Stock> producer = new KafkaProducer(props);
        producer.send(new ProducerRecord<String,Stock>("ORDER-TOPIC",stock)) ;
        System.out.println("send success");
    }
}
