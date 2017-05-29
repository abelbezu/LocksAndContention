package com.multicore;


import java.util.concurrent.atomic.*;

public class MCSLock implements Lock {
    /**
     * Add your fields here
     */
     private AtomicReference tail;
     ThreadLocal<QNode> myNode;
    public MCSLock() {
        this.tail = new AtomicReference(null);
        this.myNode = new ThreadLocal();
    }

    public void lock() {
        this.myNode.set(new QNode());
        QNode pred = (QNode) this.tail.getAndSet(myNode.get());
        if (pred != null) {
            myNode.get().locked = true;
            pred.next = myNode.get();
            while (myNode.get().locked) { // weird
            }

        }
    }

    public void unlock() {
        if (this.myNode.get().next == null) {
            if (this.tail.compareAndSet(this.myNode.get(), null))
                return;
            while (this.myNode.get().next == null) {}
        }
        (this.myNode.get()).next.locked = false;

    }

    /**
     * Checks if the calling thread observes another thread concurrently
     * calling lock(), in the critical section, or calling unlock().
     *
     * @return
     *          true if another thread is present, else false
     */
    public boolean isContended() {
        return this.tail.get() == null;
    }

    class QNode {
        volatile boolean locked = false;
        volatile QNode   next   = null;

    }
}
