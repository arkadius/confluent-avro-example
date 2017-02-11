package net.coatli.confluent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import net.coatli.confluent.domain.Item;

public class ConfluentAvroProducerApplication {

  private static final int TOTAL_ITEMS = 40;

  private static final List<Item> items = new ArrayList<>(TOTAL_ITEMS);

  static {
    for (int index = 0; index < TOTAL_ITEMS; index++) {
      items.add(
        new Item()
          .setKey(index)
          .setDescription("Chifladera " + index)
          .setCheckInDate(Calendar.getInstance().getTime()));
    }
  }

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
                            "{\"name\": \"id\", \"type\": \"int\"}," +
                            "{\"name\": \"description\", \"type\": \"string\"}," +
                            "{\"name\": \"checkin\", \"type\": \"long\"}" +
                            "]}";
    final Schema schema = new Schema.Parser().parse(schemaString);

    for (final Item item : items) {
      final GenericRecord record = new GenericData.Record(schema);
      record.put("id", item.getKey());
      record.put("description", item.getDescription());
      record.put("checkin", item.getCheckInDate().getTime());

      producer.send(new ProducerRecord<Integer, GenericRecord>("items", item.getKey(), record));
    }

    producer.close();
  }

}
