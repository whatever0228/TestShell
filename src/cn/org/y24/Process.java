package cn.org.y24;

import cn.org.y24.util.Status;
import cn.org.y24.util.StatusType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Process {
    private int pid;
    private int priority;
    static final int cpuState = 0;
    static final int memoryState = 0;
    static final int openFilesState = 0;
    Map<Integer, Integer> otherResources = new HashMap<>();

    public Status getStatus() {
        return status;
    }

    public void setStatusType(StatusType status) {
        this.status.type = status;
    }

    private Status status = new Status();
    private Process father;
    private List<Process> children;

    public int getPriority() {
        return priority;
    }

    Process(int pid, int priority) {
        this.pid = pid;
        this.priority = priority;
        father = null;
        children = new ArrayList<>();
    }

    public List<Process> getChildren() {
        return children;
    }

    public Process getFather() {
        return father;
    }

    public void setFather(Process father) {
        this.father = father;
    }

    public int getPid() {
        return pid;
    }


    void setStatusList(int rid) {
        this.status.rid = rid;
    }

    void printPCB() {
        System.out.println(String.format("priority: %d,status: %s,children: parent: ", priority, getStatusTypeStr(status)));
        System.out.println(father == null ? "No father process" : String.format("Father process pid: %d", father.pid));
        if (children.isEmpty()) System.out.println("No child process");
        else {
            System.out.print("child process pid: ");
            children.forEach(process -> System.out.print(process.getPid() + " "));
            System.out.println();
        }

        if (otherResources.isEmpty()) {
            System.out.println("No other resources yet");
        } else {
            System.out.println("Other resources: ");
        }
        otherResources.forEach((rid, count) -> {
            System.out.println(String.format("R%d: %d", rid, count));
        });
    }

    private String getStatusTypeStr(final Status status) {
        switch (status.type) {
            case ready:
                return "ready";
            case blocked:
                return "blocked at R" + status.rid;
            case running:
                return "running";
        }
        return "unknown";
    }
}
