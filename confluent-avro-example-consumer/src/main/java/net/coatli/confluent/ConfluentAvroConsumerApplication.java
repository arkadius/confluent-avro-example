package net.coatli.confluent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.kafka.common.errors.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.KafkaAvroDecoder;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.utils.VerifiableProperties;
import net.coatli.confluent.domain.Item;

public class ConfluentAvroConsumerApplication {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfluentAvroConsumerApplication.class);

  private static final String TOPIC = "items";

  public static void main(final String[] args) {

    final Properties props = new Properties();
    props.put("zookeeper.connect", "localhost:2181");
    props.put("group.id", "items_readers");
    props.put("schema.registry.url", "http://localhost:8081");

    final Map<String, Integer> topicCountMap = new HashMap<>();
    topicCountMap.put(TOPIC, new Integer(1));

    final VerifiableProperties vProps = new VerifiableProperties(props);
    final KafkaAvroDecoder keyDecoder = new KafkaAvroDecoder(vProps);
    final KafkaAvroDecoder valueDecoder = new KafkaAvroDecoder(vProps);

    final ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

    final Map<String, List<KafkaStream<Object, Object>>> consumerMap
        = consumer.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);

    final KafkaStream<Object, Object> stream = consumerMap.get(TOPIC).get(0);

    final ConsumerIterator<Object, Object> it = stream.iterator();

    while (it.hasNext()) {

      final MessageAndMetadata<Object, Object> messageAndMetadata = it.next();
      try {

        LOGGER.info("key='{}', domain={}",
            messageAndMetadata.key(), generateDomain((GenericRecord )messageAndMetadata.message()));

      } catch (final SerializationException exc) {
        LOGGER.error("Error at consuming.", exc);
      }

      consumer.commitOffsets();
    }
  }

  private static Item generateDomain(final GenericRecord genericRecord) {

    return
      new Item()
        .setKey((Integer )genericRecord.get("id"))
        .setDescription(((Utf8 )genericRecord.get("description")).toString())
        .setCheckInDate(new Date((Long )genericRecord.get("checkin")));
  }

}
