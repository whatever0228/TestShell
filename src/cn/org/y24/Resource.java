package cn.org.y24;

import cn.org.y24.util.Pair;

import java.util.LinkedList;
import java.util.List;

class Resource {
    private int id;

    int getFreeCount() {
        return freeCount;
    }

    void setFreeCount(int freeCount) {
        this.freeCount = freeCount;
    }

    List<Pair<Integer, Integer>> getWaitingList() {
        return waitingList;
    }

    private final int totalCount;
    private int freeCount;
    private List<Pair<Integer, Integer>> waitingList;

    Resource(int id, int totalCount) {
        this.id = id;
        this.totalCount = totalCount;
        this.waitingList = new LinkedList<>();
        this.freeCount = totalCount;
    }

    int getId() {
        return id;
    }

    int getTotalCount() {
        return totalCount;
    }
    void info(){
        System.out.println(String.format("R%d: total %d, free %d",id,totalCount,freeCount));
        if(waitingList.isEmpty()){
            System.out.println("No waiting list yet.");
        }
        else {
            System.out.println("Waiting list: ");
        }
    }
}
