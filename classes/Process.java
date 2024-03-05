package classes;

import app.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Process implements Runnable {
    private final String ip;
    private final int port;
    private final String id;
    private final Server server;
    private final Client client = new Client();

    public Process(String id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;

        try {
            server = new Server(port, client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    public void connectTo(Process connectTo, boolean isDefault) throws IOException {
        client.addRoute(connectTo.getServerAddress(), isDefault);
    }

    public void sendPackage(String destination, String message) {
        new Thread(() -> {
            App.incrementTasks();

            Package pack = new Package(this.getServerAddress(), destination, message);

            if (destination.equals(App.BROADCAST_ADDR))
                client.broadcast(pack, "", Record.Type.SEND);
            else
                client.sendPackage(pack, Record.Type.SEND);

            App.decrementTasks();
        }).start();
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        records.addAll(server.getRecords());
        records.addAll(client.getRecords());
        records.sort((o1, o2) -> Math.toIntExact(o1.getCreatedAt() - o2.getCreatedAt()));

        return records;
    }

    public String getServerAddress() {
        return ip + ":" + port;
    }
    public void shutDown() {
        try {
            server.shutDown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
