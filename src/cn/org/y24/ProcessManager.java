package cn.org.y24;

import cn.org.y24.util.Pair;
import cn.org.y24.util.StatusType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ProcessManager {
    ResourceManager resourceManager;
    private static int currentIndex = 0;
    private Map<Integer, Process> processPool = new HashMap<>();
    Map<Integer, String> processNamePool = new HashMap<>();
    List<Pair<Integer, Process>> readyList = new LinkedList<>();

    void createProcess(String name, int priority) {
        if (processNamePool.containsValue(name)) {
            System.out.println("Cannot create the process");
        } else {
            Process p = new Process(++currentIndex, priority);
            p.setStatusType(StatusType.ready);
            if (!readyList.isEmpty()) {
                p.setFather(readyList.get(0).second);
                readyList.get(0).second.getChildren().add(p);
            }
            readyList.add(new Pair<>(currentIndex, p));
            processPool.put(currentIndex, p);
            processNamePool.put(currentIndex, name);
            scheduler();
        }
    }

    void destroyProcess(String name) {
        int pid = -1;
        if (!processNamePool.containsValue(name)) {
            System.out.println("Cannot delete the process");
            return;
        } else {
            for (var i : processNamePool.entrySet()) {
                if (i.getValue().equals(name))
                    pid = i.getKey();
            }
        }
        Process p = processPool.remove(pid);
        Map<Integer, Integer> tmp = new HashMap<>();
        for (var i : p.otherResources.entrySet())
            tmp.put(i.getKey(), i.getValue());
        for (var i : tmp.entrySet())
            resourceManager.release(i.getKey(), i.getValue());
        processNamePool.remove(pid);
        readyList.removeIf(integerProcessPair -> integerProcessPair.second.equals(p));
    }

    void scheduler() {
        int priority = -1;
        int index = -1;
        if (readyList.isEmpty())
            return;
        if (readyList.size() == 1) {
            readyList.get(0).second.setStatusType(StatusType.running);
        }
        for (int i = 0; i < readyList.size(); i++) {
            Pair<Integer, Process> pair = readyList.get(i);
            if (pair.second.getPriority() > priority) {
                priority = pair.second.getPriority();
                index = i;
            }
        }
        Pair<Integer, Process> pair1 = readyList.remove(index);
        pair1.second.setStatusType(StatusType.running);
        readyList.add(0, pair1);

    }

    void timeOut() {
        if (readyList.isEmpty() || readyList.size() == 1)
            return;
        int priority = -1;
        int index = -1;
        for (int i1 = 1; i1 < readyList.size(); i1++) {
            Pair<Integer, Process> i = readyList.get(i1);
            if (i.second.getPriority() > priority) {
                index = i1;
                priority = i.second.getPriority();
            }
        }
        var next = readyList.remove(index);
        next.second.setStatusType(StatusType.running);
        var current = readyList.remove(0);
        readyList.add(0, next);
        readyList.add(current);
        // scheduler();
    }

    Process getProcess(int pid) {
        return processPool.get(pid);
    }

}
