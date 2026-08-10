package com.banking.operaciones.config;

import com.banking.operaciones.messaging.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic cuentasCreadasTopic() {
        return TopicBuilder.name(KafkaTopics.CUENTAS_CREADAS)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    NewTopic movimientosRealizadosTopic() {
        return TopicBuilder.name(KafkaTopics.MOVIMIENTOS_REALIZADOS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
