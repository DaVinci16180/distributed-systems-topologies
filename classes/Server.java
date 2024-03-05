package classes;

import app.App;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private final ServerSocket serverSocket;
    private final Client processClient;
    private final List<Record> serverRecords = new ArrayList<>();

    public Server(int port, Client processClient) throws IOException {
        serverSocket = new ServerSocket(port);
        this.processClient = processClient;
    }

    @Override
    public void run() {
        try {
            while (App.running) {
                Socket requester = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    App.incrementTasks();

                    try {
                        ObjectInputStream in = new ObjectInputStream(requester.getInputStream());
                        Package pack = (Package) in.readObject();

                        if (pack.getDestination().equals(App.BROADCAST_ADDR)) {
                            broadcast(pack, getServerAddress());
                        } else if (pack.getDestination().equals(this.getServerAddress())) {
                            Record record = new Record(pack, Record.Type.RECEIVE);
                            record.setContent(pack.getContent());

                            this.serverRecords.add(record);
                        } else {
                            processClient.sendPackage(pack, Record.Type.FORWARD);
                        }

                        in.close();
                        requester.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        App.decrementTasks();
                    }
                });

                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Servidor encerrado.");
        }
    }

    public List<Record> getRecords() {
        return this.serverRecords;
    }

    private void broadcast(Package pack, String forwarder) {
        Record record = new Record(pack, Record.Type.RECEIVE);
        record.setContent(pack.getContent());

        this.serverRecords.add(record);

        processClient.broadcast(pack, forwarder, Record.Type.FORWARD);
    }

    public void shutDown() throws IOException {
        serverSocket.close();
    }

    public String getServerAddress() {
        return serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();
    }
}
