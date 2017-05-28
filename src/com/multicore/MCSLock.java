package com.multicore;


import java.util.concurrent.atomic.*;

public class MCSLock implements Lock {
    /**
     * Add your fields here
     */
     private AtomicReference<QNode> tail;
     private AtomicReference<QNode> myNode;
    public MCSLock() {
        this.tail = new AtomicReference<QNode>(new QNode());
        this.myNode = new AtomicReference<QNode>(new QNode());
    }

    public void lock() {

        QNode pred = this.tail.getAndSet(this.myNode.get());
        if (pred != null) {
            this.myNode.get().locked = true;
            pred.next = this.myNode.get();
            while (pred.locked) {
            }

        }
    }

    public void unlock() {
        if (this.myNode.get().next == null) {
            if (this.tail.compareAndSet(this.myNode.get(), null))
                return;
            while (this.myNode.get().next == null) {}
        }
        this.myNode.get().next.locked = false;

    }

    /**
     * Checks if the calling thread observes another thread concurrently
     * calling lock(), in the critical section, or calling unlock().
     *
     * @return
     *          true if another thread is present, else false
     */
    public boolean isContended() {
        return false;
    }

    class QNode {
        volatile boolean locked = false;
        volatile QNode   next   = null;

    }
}
