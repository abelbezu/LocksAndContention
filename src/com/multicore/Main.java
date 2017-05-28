package com.multicore;



import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;




class PrimeCounter{
    private long count = 0;
    private CLHLock lock = new CLHLock();

    public void increment(){
        int processNum = (int)(Thread.currentThread().getId()%2);
        // critical section, lock her
        lock.lock();
        count++;
        // end of critical sectin, unlock here
        lock.unlock();
    }
    public long get(){
        return this.count;
    }
}


class Worker extends Thread{
    private AtomicInteger testNum;
    private PrimeCounter pc;
    public Worker(AtomicInteger c, PrimeCounter pc){
        testNum = c;
        this.pc = pc;
    }
    boolean isPrime(long n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (long)Math.sqrt(n)+1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if(n%(i-1) == 0 || n%(i+1) == 0) return false;
        }
        return true;
    }
    @Override
    public void run(){

        long threadId = Thread.currentThread().getId();
        while(testNum.get() <= 1000000){
            long currentNum = testNum.getAndIncrement();
            boolean verdict = isPrime(currentNum);
            if(verdict) this.pc.increment();
        }

        System.out.println(this.pc.get());


    }
}


class Counter {
    private long count;
    private long end;
    private CLHLock lock = new CLHLock();
    public Counter(long startNum, long endNum){
        count = startNum;
        end = endNum;
    }
    public   long getAndIncrement(){
        int processNum = (int)(Thread.currentThread().getId()%2);

        lock.lock();
        // critical section lock here;
        try {
            long temp = count;
            count = temp + 1;
        }finally {
            lock.unlock();
        }

        // critical section ends, unlock here
        return count;


    }
    public synchronized boolean isDone(){
        return count >= end;
    }
}


class DumbFinder{
    private long startNum;
    private long endNum;
    private PrimeCounter counter;
    public DumbFinder(long startNum, long endNum, PrimeCounter pc){
        this.startNum = startNum;
        this.endNum = endNum;
        this.counter = pc;
    }
    boolean isPrime(long n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (long)Math.sqrt(n)+1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if(n%(i-1) == 0 || n%(i+1) == 0) return false;
        }
        return true;
    }

    public long findAndCount(){
        for(long i = this.startNum; i <= this.endNum; i++){
            if(isPrime(i)) this.counter.increment();
        }
        return this.counter.get();
    }

}


public class Main {




    public static void main(String args[]) {

        PrimeCounter pc = new PrimeCounter();
        long startTwo = System.nanoTime();
        AtomicInteger coun = new AtomicInteger();
        for(int i = 0; i < 2; i++)
        {
            (new Worker(coun, pc)).start();

        }

    }
}