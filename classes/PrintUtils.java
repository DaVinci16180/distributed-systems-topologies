package classes;

import app.App;

import java.util.List;
import java.util.Scanner;

public class PrintUtils {
    public static void printRecords(List<Process> processList) {
        int empty = processList.stream().filter(p -> p.getRecords().size() - 1 < 0).toList().size();
        if (empty == processList.size())
            return;

        System.out.println("grep=cls");

        char frame = '=';
        repeat('%', 20 * processList.size() + 2, true);

        System.out.print('%');
        for (Process process : processList) {
            div(process.getId(), 20, Alignment.CENTER);
        }
        System.out.println('%');
        System.out.print('%');
        repeat(frame, 20 * processList.size(), false);
        System.out.println('%');

        int i = 0;

        while (empty < processList.size()) {
            // frame top
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    repeat('-', 18, false);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // type
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    div(process.getRecords().get(i).getType().toString(), 18, Alignment.CENTER);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // remetente
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    div(process.getRecords().get(i).getSource(), 18, Alignment.CENTER);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // seta
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    div("->", 18, Alignment.CENTER);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // destinatario
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    div(process.getRecords().get(i).getDestination(), 18, Alignment.CENTER);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // espaçador
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    div("..", 18, Alignment.CENTER);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // content
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    String content = process.getRecords().get(i).getContent();
                    System.out.print('|');
                    div(content != null ? content : "######", 18, Alignment.CENTER);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            // frame bottom
            System.out.print('%');
            for (Process process : processList) {
                if (process.getRecords().size() - 1 < i) {
                    div("", 20, Alignment.LEFT);
                } else {
                    System.out.print('|');
                    repeat('-', 18, false);
                    System.out.print('|');
                }
            }
            System.out.println('%');

            i++;
            final int current = i;
            empty = processList.stream().filter(p -> p.getRecords().size() - 1 < current).toList().size();
        }

        repeat('%', 20 * processList.size() + 2, true);
    }

    public static void processTable(List<Process> processList) {
        PrintUtils.repeat('%', 30, true);
        System.out.print("%");
        PrintUtils.div("Processos", 28, PrintUtils.Alignment.CENTER);
        System.out.println("%");
        System.out.print("%");
        PrintUtils.repeat('=', 28, false);
        System.out.println("%");

        System.out.print("%");
        PrintUtils.div("ID", 4, PrintUtils.Alignment.CENTER);
        System.out.print(":");
        PrintUtils.div("ENDERECO IP", 15, PrintUtils.Alignment.CENTER);
        System.out.print(":");
        PrintUtils.div("PORTA", 7, PrintUtils.Alignment.CENTER);
        System.out.println("%");
        System.out.print("%");
        PrintUtils.repeat('-', 28, false);
        System.out.println("%");

        for (Process process : processList) {
            System.out.print("%");
            PrintUtils.div(process.getId(), 4, PrintUtils.Alignment.CENTER);
            System.out.print(":");
            PrintUtils.div(process.getIp(), 15, PrintUtils.Alignment.CENTER);
            System.out.print(":");
            PrintUtils.div(String.valueOf(process.getPort()), 7, PrintUtils.Alignment.CENTER);
            System.out.println("%");
        }

        PrintUtils.repeat('%', 30, true);
    }

    public static void userInput(List<Process> processList) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n# Enviar pacote? [S|N] -> ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("n")) {
            App.running = false;
            processList.forEach(Process::shutDown);
            return;
        }

        PrintUtils.processTable(processList);

        List<String> validIds = processList.stream().map(Process::getId).toList();

        System.out.print("\n# Id do processo remetente -> ");
        String sourceId = scanner.nextLine();
        while (!validIds.contains(sourceId)) {
            System.out.print("Erro - Insira um id válido -> ");
            sourceId = scanner.nextLine();
        }

        System.out.print("# Id do processo destinatário (\"bc\" para broadcast) -> ");
        String destinationId = scanner.nextLine();
        while (
                !(validIds.contains(destinationId) || destinationId.equalsIgnoreCase("bc"))
                || destinationId.equals(sourceId)
        ) {
            System.out.print("Erro - Insira um id válido -> ");
            destinationId = scanner.nextLine();
        }

        String destinationAddress;
        if (destinationId.equalsIgnoreCase("bc")) {
            destinationAddress = App.BROADCAST_ADDR;
        } else {
            String finalDestinationId = destinationId;
            destinationAddress = processList
                    .stream()
                    .filter(p -> p.getId().equals(finalDestinationId))
                    .findFirst()
                    .orElseThrow()
                    .getServerAddress();
        }


        System.out.print("# Mensagem -> ");
        String message = scanner.nextLine();

        String finalSourceId = sourceId;
        processList
                .stream()
                .filter(p -> p.getId().equals(finalSourceId))
                .findFirst()
                .orElseThrow()
                .sendPackage(destinationAddress, message);
    }

    public enum Alignment { LEFT, CENTER }

    public static void div(String text, int size, Alignment alignment) {
        if (text.length() >= size - 2) {
            System.out.print(" " + text.substring(0, size - 2) + " ");
            return;
        }

        int spaces = size - text.length() - 2;

        if (spaces == 1)
            alignment = Alignment.LEFT;

        switch (alignment) {
            case LEFT -> {
                System.out.print(" " + text);
                System.out.format(String.format("%1$" + spaces + "s ", ""));
            }
            case CENTER -> {
                System.out.format(String.format(" %1$" + Math.floor(spaces / 2.) + "s", ""));
                System.out.print(text);
                System.out.format(String.format("%1$" + Math.ceil(spaces / 2.) + "s ", ""));
            }
        }
    }

    public static void repeat(char ch, int times, boolean br) {
        System.out.print(String.format("%1$" + times + "s", "").replace(' ', ch));
        if (br)
            System.out.println();
    }
}
