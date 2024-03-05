package classes;

import app.App;

import java.util.ArrayList;
import java.util.List;

public class RoutingTable {
    List<String> addresses = new ArrayList<>();
    String defaultAddr;

    public void add(String addr, boolean isDefault) {
        if (isDefault)
            defaultAddr = addr;

        addresses.add(addr);
    }

    public List<String> getRoutes(String destination) {
        if (destination.equals(App.BROADCAST_ADDR)) {
            return new ArrayList<>(addresses);
        }

        List<String> result = new ArrayList<>(addresses.stream().filter(a -> a.equals(destination)).toList());

        if (result.isEmpty()) {
            result.add(defaultAddr);
        }

        return result;
    }

}
