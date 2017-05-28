package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by abelbezu on 5/28/17.
 */
class Counter {
    private AtomicInteger count;
    private long end;

    public Counter(int startNum, int endNum){
        count = new AtomicInteger(startNum);
        end = endNum;
    }
    public   long getAndIncrement(){
        return this.count.getAndIncrement();


    }

    public int get(){
        return this.count.get();
    }
    public synchronized boolean isDone(){
        return this.count.get() >= this.end;
    }
}
