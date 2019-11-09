package cn.org.y24;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class OS {
    private boolean isInit = false;
    private boolean isFileInput;
    private String fileName;
    private ProcessManager processManager;
    private ResourceManager resourceManager;
    private Scanner scanner;

    OS(boolean isFileInput, String fileName) {
        this.isFileInput = isFileInput;
        this.fileName = fileName;
    }

    OS() {
        this.isFileInput = false;
    }

   /* public void checkIfInit() {
        if (!isInit) {
            System.out.println("Not Init!");
            System.exit(1);

        }
    }*/

    void mainLoop() throws FileNotFoundException {
        init();
        while (true) {
            displayShellSymbol();
            String commandStr = scanner.next();
            switch (commandStr) {
                case "quit":
                    quit();
                case "cr":
                    String processName0 = scanner.next();
                    int priority = scanner.nextInt();
                    createProcess(processName0, priority);
                    displayRunningProcess();
                    continue;
                case "de":
                    String processName1 = scanner.next();
                    destroyProcess(processName1);
                    displayRunningProcess();
                    continue;
                case "to":
                    timeOut();
                    displayRunningProcess();
                    continue;
                case "req":
                    String resStr = scanner.next();
                    int count = scanner.nextInt();
                    int rid = Character.getNumericValue(resStr.charAt(resStr.length() - 1));
                    requestResource(rid, count);
                    displayRunningProcess();
                    continue;
                case "pr":
                    String processName2 = scanner.next();
                    printPCB(processName2);
                    continue;
                case "list":
                    switch (scanner.next()) {
                        case "ready":
                            listReady();
                            break;
                        case "block":
                            listBlocked();
                            break;
                        case "res":
                            listRes();
                            break;
                        default:
                            System.out.println("No suit list ");
                            System.exit(1);
                    }
                    continue;
                default:
                    System.out.println("No suit command " + commandStr);
                    System.exit(1);
            }
        }
    }

    private void displayShellSymbol() {
        if (!isFileInput)
            System.out.print("OS shell:>");
    }

    private void println(Object s) {
        System.out.println(s);
    }

    private void quit() {
        System.out.println("See you next time.");
        System.exit(0);
    }

    private void displayRunningProcess() {
        if (processManager.readyList.size() > 0)
            println(String.format("process %s is running", processManager.processNamePool.get(processManager.readyList.get(0).first)));
    }

    private void init() throws FileNotFoundException {
        displayShellSymbol();
        if (!isInit) {
            isInit = true;
            if (isFileInput) {
                scanner = new Scanner(new File(fileName));
            } else {
                scanner = new Scanner(System.in);
            }
            scanner.next();
            displayShellSymbol();
            if (!isFileInput) System.out.print("input init args: ");
            int[] args = new int[4];
            for (int i = 0; i < args.length; i++)
                args[i] = scanner.nextInt();
            processManager = new ProcessManager();
            resourceManager = new ResourceManager(args);
            processManager.resourceManager = resourceManager;
            resourceManager.processManager = processManager;
            processManager.createProcess("init", 0);
            displayRunningProcess();
        }

    }

    private void createProcess(String name, int priority) {
        processManager.createProcess(name, priority);
    }

    private void destroyProcess(String name) {
        processManager.destroyProcess(name);
    }

    private void requestResource(int rid, int count) {
        resourceManager.request(rid, count);
    }


    private void timeOut() {
        processManager.timeOut();
    }

    private void listReady() {
        println("Ready list:");
        processManager.readyList.forEach(integerProcessPair ->
                System.out.printf("%d: %s(%d) -> ", integerProcessPair.second.getPriority(), processManager.processNamePool.get(integerProcessPair.first), integerProcessPair.first));
        println("");
    }

    private void listRes() {
        resourceManager.resourcePool.forEach((Resource::info));
    }

    private void listBlocked() {
        println("Blocked list:");
        resourceManager.resourcePool.forEach(resource -> {
            if (!resource.getWaitingList().isEmpty()) {
                resource.getWaitingList().forEach(pair -> {
                    final String name = processManager.processNamePool.get(pair.first);
                    println(String.format("R%s: %s", resource.getId(), name));
                });
            }
        });
    }

    private void printPCB(String name) {
        int pid = -1;
        if (!processManager.processNamePool.containsValue(name)) {
            System.out.println("Cannot print the PCB of the process");
            return;
        } else {
            for (var i : processManager.processNamePool.entrySet()) {
                if (i.getValue().equals(name))
                    pid = i.getKey();
            }
        }
        Process process = processManager.getProcess(pid);
        process.printPCB();
    }
}
