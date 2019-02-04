Things to consider and improve on
---

Consider implementing a Buffer object using concurrent lock `java.util.concurrent.locks`. Consider signalling threads to continue consuming as long as the buffer is not full, and if the buffer is full pause threads while the queue performs maintenance.

Consider implementing the buffer such that it stores Kafka records, consider implementing the buffer such that those Kafka records a1e in a static queue (shared between all publishers and consumers) and we create RecordPublishers responsible for publishing records to the queue. 

Consider writing whether the queue contains data to a boolean to avoid repetitively accessing the Queue object.