package classes;

import java.util.Date;

public class Record {

    public enum Type {
        SEND,
        RECEIVE,
        FORWARD
    }
    private final Type type;
    private String content;
    private final String source;
    private final String destination;
    private final long createdAt = new Date().getTime();

    public Record(Package pack, Type type) {
        this.source = pack.getSource();
        this.destination = pack.getDestination();
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Record{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
