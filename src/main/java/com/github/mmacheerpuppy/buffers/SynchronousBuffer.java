package com.github.mmacheerpuppy.buffers;

/**
 * Responsible for writing messages to a Java class and providing semantics to retrieve those messages.
 * A buffer implies a capacity for the buffer, so an implementation of whether the buffer is full must be provided.
 */
public interface SynchronousBuffer<E> {
    /**
     * Asynchronous method to check data exists in the queue.
     *
     * @return Returns true if there is data.
     */
    public boolean hasData();

    /**
     * Synchronous method to add data to the buffer.
     */
    public void put (E object) throws InterruptedException;

    /**
    * Asynchronous method to determine whether the buffer implementation is at capacity.
    *
    * @return Returns true if there is data.
    */
    public boolean isFull();

}
