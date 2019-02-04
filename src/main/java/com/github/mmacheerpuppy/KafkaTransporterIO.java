package com.github.mmacheerpuppy;

import com.github.mmacheerpuppy.buffers.RecordBuffer;
import com.github.mmacheerpuppy.buffers.SynchronousBuffer;
import com.github.mmacheerpuppy.consumers.RawSocketPublisher;
import org.apache.kafka.clients.producer.ProducerRecord;


/**
 * Practice project responsible for transporting project data from hard-coded web sockets to Kafka.
 * May in future evolve to a configurable application.
 */
public class KafkaTransporterIO {
    public static void main(String[] args) {
        SynchronousBuffer<ProducerRecord> buffer = new RecordBuffer<>(100);
        Thread t1 = new Thread(new RawSocketPublisher(buffer, "wss://www.destiny.gg/ws"));
        t1.start();
    }
}
