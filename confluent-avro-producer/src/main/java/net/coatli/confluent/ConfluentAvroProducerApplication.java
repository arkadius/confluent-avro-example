package net.coatli.confluent;

import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ConfluentAvroProducerApplication {

  private static final int TOTAL_ITEMS = 40;

  public static void main(final String[] args) {

    final Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    props.put("schema.registry.url", "http://localhost:8081");
    final Producer<Integer, GenericRecord> producer = new KafkaProducer<Integer, GenericRecord>(props);

    final String schemaString =
                            "{\"type\": \"record\", " +
                            "\"name\": \"item\"," +
                            "\"fields\": [" +
                            "{\"name\": \"key\", \"type\": \"int\"}," +
                            "{\"name\": \"description\", \"type\": \"string\"}" +
                            "]}";
    final Schema schema = new Schema.Parser().parse(schemaString);

    for (int index = 0; index < TOTAL_ITEMS; index++) {
      final GenericRecord record = new GenericData.Record(schema);
      record.put("key", index);
      record.put("description", "Item " + index);

      producer.send(new ProducerRecord<Integer, GenericRecord>("items", index, record));
    }

    producer.close();
  }
}
