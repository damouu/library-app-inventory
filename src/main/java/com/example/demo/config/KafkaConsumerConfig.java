package com.example.demo.config;

import com.example.demo.dto.BorrowEventPayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> baseConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        return props;
    }

    @Bean
    public ConsumerFactory<UUID, BorrowEventPayload> consumerFactory() {

        UUIDDeserializer keyDelegate = new UUIDDeserializer();
        ErrorHandlingDeserializer<UUID> keyDeserializer = new ErrorHandlingDeserializer<>(keyDelegate);

        JsonDeserializer<BorrowEventPayload> jsonDeserializer = new JsonDeserializer<>(BorrowEventPayload.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.addTrustedPackages("*");
        ErrorHandlingDeserializer<BorrowEventPayload> valueDeserializer = new ErrorHandlingDeserializer<>(jsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(baseConsumerConfig(), keyDeserializer, valueDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<UUID, BorrowEventPayload>> factory(ConsumerFactory<UUID, BorrowEventPayload> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<UUID, BorrowEventPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.demo.dto.BorrowEventPayload");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.demo.dto");

        return props;
    }
}