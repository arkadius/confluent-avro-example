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
import org.apache.kafka.clients.producer.ProducerConfig;

import net.coatli.confluent.domain.Item;

public class ConfluentAvroProducerApplication {

  public static void main(final String[] args) {

    int FIELDS = Integer.parseInt(args[0]);
    int TOTAL_ITEMS = Integer.parseInt(args[1]);
    int BATCH = Integer.parseInt(args[2]);
    int THREADS = Integer.parseInt(args[3]);

    final Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
    props.put("schema.registry.url", "http://localhost:8081");
    props.put(ProducerConfig.LINGER_MS_CONFIG, BATCH);
    
    final Producer<Integer, GenericRecord> producer = new KafkaProducer<Integer, GenericRecord>(props);

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < FIELDS; i++) {
      sb.append("{\"name\": \"field");
      sb.append(i);
      sb.append("\", \"type\": \"int\"}");
      if (i != FIELDS-1)
        sb.append(",");
    }

    final String schemaString =
                            "{\"type\": \"record\", " +
                            "\"name\": \"item\"," +
                            "\"fields\": [" +
                            sb +
                            "]}";
    final Schema schema = new Schema.Parser().parse(schemaString);


    final GenericRecord record = new GenericData.Record(schema);
    for (int i = 0; i < FIELDS; i++) {
      record.put("field" + i, i);
    }

    long before = System.currentTimeMillis();
    for (int i = 0; i < TOTAL_ITEMS; i++) {
      producer.send(new ProducerRecord<Integer, GenericRecord>("items" + FIELDS, i, record));
    }

    producer.close();
    System.out.println("Sending " + TOTAL_ITEMS + " elements took: " + (System.currentTimeMillis() - before));
  }

}
