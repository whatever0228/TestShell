package cn.org.y24;

import cn.org.y24.util.Pair;
import cn.org.y24.util.StatusType;

import java.util.ArrayList;
import java.util.List;

class ResourceManager {
    List<Resource> resourcePool = new ArrayList<>();
    ProcessManager processManager;

    ResourceManager(int[] max) {
        for (int i = 1; i <= 4; i++)
            resourcePool.add(new Resource(i, max[i - 1]));
    }
    void request(int rid, int count) {
        Resource resource = resourcePool.get(rid-1);
        Process process = processManager.readyList.get(0).second;
        final int pid = processManager.readyList.get(0).first;
        if (resource.getFreeCount() >= count) {
            resource.setFreeCount(resource.getFreeCount() - count);
            process.otherResources.put(rid, count);
        } else {
            if (count > resource.getTotalCount())
                System.exit(1);
            process.setStatusType(StatusType.blocked);
            process.setStatusList(rid);
            resource.getWaitingList().add(new Pair<>(pid, count));
            processManager.readyList.remove(0);
            processManager.scheduler();
        }
    }

    void release(int rid, int count) {
        Resource resource = resourcePool.get(rid-1);
        Process process = processManager.readyList.get(0).second;
        process.otherResources.remove(rid);
        resource.setFreeCount(resource.getFreeCount() + count);
        while (!resource.getWaitingList().isEmpty() && resource.getFreeCount() >= resource.getWaitingList().get(0).second) {
            resource.setFreeCount(resource.getFreeCount() - resource.getWaitingList().get(0).second);
            Pair<Integer, Integer> q = resource.getWaitingList().remove(0);
            Process p = processManager.getProcess(q.first);
            p.setStatusType(StatusType.ready);
            p.otherResources.put(rid, q.second);
            processManager.readyList.add(0, new Pair<>(q.first, p));
            processManager.scheduler();
        }
    }
}
