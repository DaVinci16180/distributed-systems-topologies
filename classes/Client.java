package classes;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    RoutingTable routes = new RoutingTable();
    private final List<Record> clientRecords = new ArrayList<>();

    public synchronized void sendPackage(Package pack, Record.Type type) {
        for (String destination : routes.getRoutes(pack.getDestination())) {
            send(pack, destination);
            this.clientRecords.add(new Record(pack, type));
        }
    }

    public synchronized void broadcast(Package pack, String forwarder, Record.Type type) {
        List<String> targets = routes.getRoutes(pack.getDestination());
        targets = targets
                .stream()
                .filter(r -> !r.equals(pack.getSource()))
                .filter(r -> !r.equals(pack.getHeaders().get("forwarder")))
                .toList();

        pack.getHeaders().put("forwarder", forwarder);

        for (String destination : targets) {
            send(pack, destination);
            this.clientRecords.add(new Record(pack, type));
        }
    }

    private void send(Package pack, String to) {
        String ip = to.split(":")[0];
        int port = Integer.parseInt(to.split(":")[1]);

        try(Socket socket = new Socket(ip, port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(pack);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Record> getRecords() {
        return this.clientRecords;
    }

    public void addRoute(String addr, boolean isDefault) {
        routes.add(addr, isDefault);
    }
}
