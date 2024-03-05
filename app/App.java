package app;

import classes.PrintUtils;
import classes.Process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static boolean running = true;
    public static int runningTasks = 0;
    public static final String BROADCAST_ADDR = "255.255.255.255:0";
    private static final List<Process> processList = new ArrayList<>();

    public static void main(String[] args) {
        star(4);

        for (Process process : processList) {
            Thread thread = new Thread(process);
            thread.start();
        }


        while (running) {
            if (runningTasks == 0) {
                PrintUtils.printRecords(processList);
                PrintUtils.userInput(processList);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void ring(int nodes) {
        for (int i = 0; i < nodes; i++) {
            processList.add( new Process("p" + (i + 1), "0.0.0.0", 8080 + i) );
        }

        try {
            for (int i = 0; i < processList.size(); i++) {
                processList.get(i).connectTo(processList.get((i + 1) % processList.size()), true);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private static void star(int nodes) {
        for (int i = 0; i < nodes; i++) {
            processList.add( new Process("p" + (i + 1), "0.0.0.0", 8080 + i) );
        }

        try {
            for (int i = 1; i < processList.size(); i++) {
                processList.get(i).connectTo(processList.get(0), true);
                processList.get(0).connectTo(processList.get(i), false);
            }
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static synchronized void incrementTasks() {
        runningTasks++;
    }


    public static synchronized void decrementTasks() {
        runningTasks--;
    }
}
