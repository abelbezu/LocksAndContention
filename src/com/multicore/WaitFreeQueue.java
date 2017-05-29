package com.multicore;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by abelbezu on 5/28/17.
 */
interface SeqentialObject{
    public abstract Response apply(Invocation invoc);
}
class Response{
    public Object value;
}
class Invocation{
    public String method;
    public Object[] args;
}



abstract class Consensus<T>{

    protected ArrayList<T> proposed = new ArrayList<>();

    protected void propose(T value) {

        proposed.add(((int)Thread.currentThread().getId()%2), value);
    }


    public abstract Object decide(Object value);
}
class Node<T> implements Comparable{
    public Invocation invoc;
    public Consensus<Node> decideNext;
    public Node next;
    public int seq;
    public Node(Invocation invoc) {
        invoc = invoc;
        decideNext = new ConsensusProtocol<Node>() {
            @Override
            public Object decide(Object value) {
                return null;
            }

            @Override
            public Node decide(Node value) {
                return null;
            }
        }
        seq = 0;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
class WaitFreeQueue<T> {
    /**
     * Add your fields here
     */
    private AtomicReference<LinkedListNode<T>> queue;
    private AtomicIntegerArray state;
    @SuppressWarnings({"unchecked"})
    public WaitFreeQueue(int capacity) {
        state = new AtomicIntegerArray(capacity);
        this.queue = new AtomicReference<LinkedListNode<T>>();
    }
    public void enq(T x) throws FullException {
        //TODO: Implement me!
    }
    public T deq() throws EmptyException {
        //TODO: Implement me!
    }
}


class FullException extends Exception {
    private static final long serialVersionUID = 1L;
    public FullException() {
        super();
    }
}

class EmptyException extends Exception {
    private static final long serialVersionUID = 1L;
    public EmptyException() {
        super();
    }
}

