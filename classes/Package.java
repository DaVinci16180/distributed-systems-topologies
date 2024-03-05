package classes;

import java.io.Serializable;
import java.util.HashMap;

public class Package implements Serializable {
    private final String source;
    private final String destination;
    private final HashMap<String, String> headers = new HashMap<>();
    private final String content;

    public Package(String source, String destination, String content) {
        this.source = source;
        this.destination = destination;
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}