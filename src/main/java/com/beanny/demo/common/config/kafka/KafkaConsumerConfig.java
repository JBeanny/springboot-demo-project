package com.beanny.demo.common.config.kafka;

import com.beanny.demo.event.model.PaymentEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String BOOSTRAP_SERVER;
    
    @Bean
    public <T> ConsumerFactory<String, T> consumerFactory(Class<T> targetType) {
        Map<String, Object> configProps = new HashMap<>();
        
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.BOOSTRAP_SERVER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(targetType);
        deserializer.addTrustedPackages("*");
        
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }
    
    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(Class<T> taretType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.consumerFactory(taretType));
        
        return factory;
    }
    
    @Bean(name = "paymentContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> paymentContainerFactory() {
        return kafkaListenerContainerFactory(PaymentEvent.class);
    }
}
