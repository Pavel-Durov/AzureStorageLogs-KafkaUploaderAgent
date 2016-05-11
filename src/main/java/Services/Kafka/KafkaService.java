package Services.Kafka;

import Consts.AgentConfig;

import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class KafkaService {

    KafkaProducer<String, byte[]> _producer;

    public static final String LOG_TOPIC = "kafkatest";

    public KafkaService(){
        Properties props = new Properties();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, GetKafkaConnection());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, "3");
        props.put(org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG, "all");
        props.put(org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, 200);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BLOCK_ON_BUFFER_FULL_CONFIG, true);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");

        _producer = new KafkaProducer<String, byte[]>(props);
    }

    public boolean SendMessage(String message, String topic) {
        boolean result = false;

        if (message != null) {
            try {
                _producer.send(new ProducerRecord<String, byte[]>(topic, "msgKey", message.getBytes())).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                _producer.close();
            }

            result = true;

        }
        return result;
    }

    private String GetKafkaConnection() {
        String ip = AgentConfig.newInstance().GetKafkaIp();
        String port = AgentConfig.newInstance().GetKafkaPort();

        return ip + ":" + port;
    }

    public String[] ReadMessages() {
        return null;
    }


    public static long getLastOffset(SimpleConsumer consumer, String topic, int partition,
                                     long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        kafka.javaapi.OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            System.out.println("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }


}
