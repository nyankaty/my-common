package com.company.common.kafka.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public final KafkaTemplate<String, String> kafkaTemplate;

    public final ObjectMapper objectMapper = new ObjectMapper();

    protected KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        this.kafkaTemplate = kafkaTemplate;
    }

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
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                public void onSuccess(SendResult<String, String> result) {
                    KafkaPublisher.log.info("===> Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "] to topic: " + topic + " SUCCESS !!!");
                    callback.onSuccess(message);
                }

                public void onFailure(Throwable ex) {
                    KafkaPublisher.log.info("xxxx> Unable to send message=[" + message + "] to topic: " + topic + " FAIL !!! \n Due to : " + ex.getMessage(), ex);
                    callback.onFailure(ex);
                }
            });
        }

    }

    public <T> void pushAsync(T payload, String topic, ListenableFutureCallback<String> callback) {
        this.sendMessageAsync(payload, topic, callback);
    }

    public <T> void pushAsync(T payload, String topic) {
        this.sendMessageAsync(payload, topic, (ListenableFutureCallback)null);
    }

    public <T> boolean pushSync(T payload, String topic) {
        return this.sendMessageSync(payload, topic, new HashMap(), (Integer)null);
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

        ProducerRecord rc;
        if (partition == null) {
            rc = new ProducerRecord(topic, message);
        } else {
            rc = new ProducerRecord(topic, partition, "", message);
        }

        if (!headers.isEmpty()) {
            Iterator var7 = headers.entrySet().iterator();

            while(var7.hasNext()) {
                Map.Entry<String, byte[]> item = (Map.Entry)var7.next();
                rc.headers().add(new RecordHeader((String)item.getKey(), (byte[])item.getValue()));
            }
        }

        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(rc);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            public void onSuccess(SendResult<String, String> result) {
                KafkaPublisher.log.info("===> Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "] to topic: " + topic + " SUCCESS !!!");
            }

            public void onFailure(Throwable ex) {
                KafkaPublisher.log.info("xxxx> Unable to send message=[" + message + "] to topic: " + topic + " FAIL !!! \n Due to : " + ex.getMessage(), ex);
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
