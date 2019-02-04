package com.github.mmacheerpuppy.consumers;

import com.github.mmacheerpuppy.buffers.SynchronousBuffer;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

/**
 * Responsible for consuming messages from a websocket and writes messages to a buffer (via object composition).
 */
public class RawSocketPublisher implements Runnable {
    // Pool of messages for Kafka producer records.
    private final SynchronousBuffer<ProducerRecord> recordBuffer;
    // Default string for the websocket.
    private final String address;
    private boolean running = false;


    /**
     * @param recordBuffer A SynchronousBuffer to prepend messages to.
     * @param address      The address of the websocket to connect to.
     */
    public RawSocketPublisher(SynchronousBuffer<ProducerRecord> recordBuffer, String address) {
        this.recordBuffer = recordBuffer;
        this.address = address;
    }

    /**
     * Example Execution of the Raw Socket Publisher.
     */
    public void run() {
        // Retrieve a static builder from the HTTP method.
        running = true;
        // Note websocket is not an auto-closable type.
        // Should probably check here that ws is executing at runtime.

        // Specify config for the AsyncHttpClient (in particular since we exceed the maximum frame length when using the destinygg ws).
        AsyncHttpClientConfig asyncHttpClientConfig = new DefaultAsyncHttpClientConfig.Builder()
                .setWebSocketMaxFrameSize(65536).build();
        // Instance a new asyncHttpClient
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(asyncHttpClientConfig);
        try {
            asyncHttpClient.prepareGet(address)
                    .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(
                            new WebSocketListener() {

                                @Override
                                public void onOpen(WebSocket webSocket) {
                                }

                                @Override
                                public void onClose(WebSocket webSocket, int i, String s) {

                                }

                                @Override
                                public void onError(Throwable throwable) {

                                }

                                @Override
                                public void onTextFrame(String payload, boolean finalFragment, int rsv) {
                                    onMessage(payload);
                                }
                            }).build()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
    }

    /**
     * The callback passed to the Websocket adapter (what to do for each inbound message).
     *
     * @param message The message
     */
    // This method should be designed to be overridden (maybe use the Strategy pattern here, or Lambda functions).
    private static void onMessage(String message) {
        System.out.println(message);
    }

}

