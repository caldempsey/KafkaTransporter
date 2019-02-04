package com.github.mmacheerpuppy.validators;

public class WebsocketValidator {
    /**
     * Determines whether a url is prepended with a valid protocol "wss://".
     * To maintain method granularity and re-usability this does not determine whether the URL contains a valid resource.
     *
     * @param url The URL of the String.
     * @return returns true if the url contains a valid protocol.
     */
    public static boolean validProtocol(String url) {
        // Case where the minimum length for the protocol is not met.
        if (url.length() < 6) {
            return false;
        }
        // Check whether the protocol is represented by a valid String.
        return url.substring(0, 5).equals("wss://");
    }
}
