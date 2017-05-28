package com.multicore;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by abelbezu on 5/28/17.
 */
        import java.util.concurrent.atomic.*;

public class CLHLock implements Lock {
    /**
     * Add your fields here
     */
    private AtomicReference<QNode> holdMyPred;
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myThreadLocalNode = new ThreadLocal<QNode>();

    public CLHLock() {
        this.tail = new AtomicReference<QNode>();
    }

    public void lock() {
        QNode pred
                = tail.getAndSet(myThreadLocalNode.get());
        this.holdMyPred.set(pred);
        while (pred.isLocked()) {}

    }

    public void unlock() {
        myThreadLocalNode.get().setLocked(false);
        myThreadLocalNode.set(holdMyPred.get());
    }

    /**
     * Checks if the calling thread observes another thread concurrently
     * calling lock(), in the critical section, or calling unlock().
     *
     * @return
     *          true if another thread is present, else false
     */
    public boolean isContended() {
        return myThreadLocalNode.get().isLocked();
    }

    private class QNode {
        /**
         * Add your fields here
         */
        private AtomicBoolean locked =
                new AtomicBoolean(true);

        public boolean isLocked(){
            return this.locked.get();
        }

        public void setLocked(boolean value){
            this.locked.set(value);

        }
    }
}

