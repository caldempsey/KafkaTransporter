package com.github.mmacheerpuppy.buffers;

import java.util.concurrent.locks.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Responsible for storing a thread safe buffer of data.
 * Another possible implementation could be to use an ArrayBlockingQueue (but we would still need to track maximum records).
 * An implementation providing a limit on the maximum number records is necessary to prevent overflow when dealing with a constant stream of input data.
 */
public class RecordBuffer<E> implements SynchronousBuffer<E> {
    private final int maximumRecords;
    private Queue<E> data = new LinkedList<E>();
    // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Condition.html
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public RecordBuffer(int maximumRecords) {
        this.maximumRecords = maximumRecords;
    }

    public E get() throws InterruptedException {
        try {
            // Lock threads while the buffer is empty.
            lock.lock();
            while (!hasData()) {
                notEmpty.await();
            }
            // Signal all threads waiting on the notFull condition.
            notFull.signalAll();
            return data.poll();
        } finally {
            // Unlock blocking threads according to the condition signals.
            lock.unlock();
        }
    }

    public boolean hasData() {
        return data.peek() != null;
    }

    public void put(E obj) throws InterruptedException {
        // Lock threads while the buffer is full.
        lock.lock();
        try {
            while (isFull()) {
                System.out.println("Queue is full - " +
                        Thread.currentThread().getName() +
                        " is waiting");
                notFull.await();
            }
            data.add(obj);
            // Signal all threads waiting on the notEmpty condition.
            notEmpty.signalAll();
        } finally {
            // Unlock blocking threads according to the conditions above.
            lock.unlock();
        }
    }

    public boolean isFull() {
        return maximumRecords <= data.size();
    }

}
