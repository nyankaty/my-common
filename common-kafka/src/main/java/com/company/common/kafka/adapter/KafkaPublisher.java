package com.company.common.kafka.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@ConditionalOnProperty(
        value = {"spring.kafka.producer.enabled"},
        havingValue = "true"
)
public abstract class KafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaPublisher.class);

    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));

    private <T> void sendMessageAsync(T data, final String topic, final ListenableFutureCallback<String> callback) {
        final String message;
        try {
            message = this.objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException var6) {
            log.error("Exception when parse data to json {}", var6.getMessage());
            return;
        }

        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, message);
        if (callback != null) {
            future.addCallback(new ListenableFutureCallback<>() {
                public void onSuccess(SendResult<String, String> result) {
                    log.info("===> Sent message=[{}] with offset=[{}] to topic: {} SUCCESS !!!", message, result.getRecordMetadata().offset(), topic);
                    callback.onSuccess(message);
                }

                public void onFailure(Throwable ex) {
                    log.info("xxx> Unable to send message=[" + message + "] to topic: " + topic + " FAIL !!! \n Due to : " + ex.getMessage(), ex);
                    callback.onFailure(ex);
                }
            });
        }
    }

    public <T> void pushAsync(T payload, String topic, ListenableFutureCallback<String> callback) {
        this.sendMessageAsync(payload, topic, callback);
    }

    public <T> void pushAsync(T payload, String topic) {
        this.sendMessageAsync(payload, topic, null);
    }

    public <T> boolean pushSync(T payload, String topic) {
        return this.sendMessageSync(payload, topic, new HashMap<>(), null);
    }

    public <T> boolean pushSync(T payload, String topic, Map<String, byte[]> headers, Integer partition) {
        return this.sendMessageSync(payload, topic, headers, partition);
    }

    private <T> boolean sendMessageSync(T data, final String topic, Map<String, byte[]> headers, Integer partition) {
        final String message;
        try {
            message = this.objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException var10) {
            log.error("Exception when parse data to json {}", var10.getMessage());
            return false;
        }

        ProducerRecord<String, String> rc;
        if (partition == null) {
            rc = new ProducerRecord<>(topic, message);
        } else {
            rc = new ProducerRecord<>(topic, partition, "", message);
        }

        if (!headers.isEmpty()) {
            headers.forEach((k, v) -> rc.headers().add(new RecordHeader(k, v)));
        }

        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(rc);
        future.addCallback(new ListenableFutureCallback<>() {
            public void onSuccess(SendResult<String, String> result) {
                log.info("===> Sent message=[{}] with offset=[{}] to topic: {} SUCCESS !!!", message, result.getRecordMetadata().offset(), topic);
            }

            public void onFailure(Throwable ex) {
                log.info("xxx> Unable to send message=[" + message + "] to topic: " + topic + " FAIL !!! \n Due to : " + ex.getMessage(), ex);
            }
        });

        try {
            future.get();
            return true;
        } catch (InterruptedException | ExecutionException var9) {
            log.error("Send Message Async exception: {}", var9.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
