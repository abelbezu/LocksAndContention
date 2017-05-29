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
    private ThreadLocal<QNode> localPred;
    private AtomicReference<QNode> tail;
    private ThreadLocal<QNode> myNode;

    public CLHLock() {
        this.myNode = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return new QNode();
            }
        };
        this.localPred = new ThreadLocal<QNode>() {
            protected QNode initialValue() {
                return null;
            }
        };
        this.tail =  new AtomicReference<>(new QNode());
    }

    public void lock() {
        QNode qNode = new QNode();
        this.myNode.set(qNode);
        QNode pred = new QNode();
        this.localPred.set(qNode);
        localPred.set(tail.getAndSet(myNode.get()));
        while (pred.isLocked()) {}

    }

    public void unlock() {
        QNode qNode = myNode.get();
        qNode.setLocked(false);
        myNode.set(localPred.get());
    }

    /**
     * Checks if the calling thread observes another thread concurrently
     * calling lock(), in the critical section, or calling unlock().
     *
     * @return
     *          true if another thread is present, else false
     */
    public boolean isContended() {
        return this.tail.get() == null; //TODO: Revisit this
    }

    private class QNode {
        /**
         * Add your fields here
         */
        private AtomicBoolean locked =
                new AtomicBoolean(false);

        public boolean isLocked(){
            return this.locked.get();
        }

        public void setLocked(boolean value){
            this.locked.set(value);

        }
    }
}

