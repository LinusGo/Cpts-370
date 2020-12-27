/**
 * @author LinusW
 * @create 2020-10-20-6:16 PM
 */
import java.util.*;

public class Scheduler extends Thread {
    // private Vector Queue0;
    // time quantum: 500ms
    private Vector Queue0;
    // time quantum: 1000ms
    private Vector Queue1;
    // time quantum: 2000ms
    private Vector Queue2;

    private int timeSlice;
    private static final int DEFAULT_TIME_SLICE = 1000;

    // New data added to p161
    private boolean[] tids; // Indicate which ids have been used
    private static final int DEFAULT_MAX_THREADS = 10000;

    // A new feature added to p161
    // Allocate an ID array, each element indicating if that id has been used
    private int nextId = 0;

    private void initTid(int maxThreads) {
        tids = new boolean[maxThreads];
        for (int i = 0; i < maxThreads; i++)
            tids[i] = false;
    }

    // A new feature added to p161
    // Search an available thread ID and provide a new thread with this ID
    private int getNewTid() {
        for (int i = 0; i < tids.length; i++) {
            int tentative = (nextId + i) % tids.length;
            if (tids[tentative] == false) {
                tids[tentative] = true;
                nextId = (tentative + 1) % tids.length;
                return tentative;
            }
        }
        return -1;
    }

    // A new feature added to p161
    // Return the thread ID and set the corresponding tids element to be unused
    private boolean returnTid(int tid) {
        if (tid >= 0 && tid < tids.length && tids[tid] == true) {
            tids[tid] = false;
            return true;
        }
        return false;
    }

    // A new feature added to p161
    // Retrieve the current thread's TCB from the Queue0
    public TCB getMyTcb() {
        Thread myThread = Thread.currentThread(); // Get my thread object
        synchronized (Queue0) {
            for (int i = 0; i < Queue0.size(); i++) {
                TCB tcb = (TCB) Queue0.elementAt(i);
                Thread thread = tcb.getThread();
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }

        synchronized (Queue1) {
            for (int i = 0; i < Queue1.size(); i++) {
                TCB tcb = (TCB) Queue1.elementAt(i);
                Thread thread = tcb.getThread();
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }

        synchronized (Queue2) {
            for (int i = 0; i < Queue2.size(); i++) {
                TCB tcb = (TCB) Queue2.elementAt(i);
                Thread thread = tcb.getThread();
                if (thread == myThread) // if this is my TCB, return it
                    return tcb;
            }
        }

        return null;
    }

    // A new feature added to p161
    // Return the maximal number of threads to be spawned in the system
    public int getMaxThreads() {
        return tids.length;
    }

    public Scheduler() {
        timeSlice = DEFAULT_TIME_SLICE;
        Queue0 = new Vector();
        Queue1 = new Vector();
        Queue2 = new Vector();
        initTid(DEFAULT_MAX_THREADS);
    }

    public Scheduler(int quantum) {
        timeSlice = quantum;
        Queue0 = new Vector();
        Queue1 = new Vector();
        Queue2 = new Vector();
        initTid(DEFAULT_MAX_THREADS);
    }

    // A new feature added to p161
    // A constructor to receive the max number of threads to be spawned
    public Scheduler(int quantum, int maxThreads) {
        timeSlice = quantum;
        Queue0 = new Vector();
        Queue1 = new Vector();
        Queue2 = new Vector();

        initTid(maxThreads);
    }

    private void schedulerSleep() {
        try {
            if (Queue0.size() != 0) {
                Thread.sleep(timeSlice / 2);

            } else if (Queue1.size() != 0) {
                Thread.sleep(timeSlice);

            } else {
                Thread.sleep(timeSlice * 2);
            }
        } catch (InterruptedException e) {
        }
    }

    // A modified addThread of p161 example
    public TCB addThread(Thread t) {
        // t.setPriority( 2 );
        TCB parentTcb = getMyTcb(); // get my TCB and find my TID
        int pid = (parentTcb != null) ? parentTcb.getTid() : -1;
        int tid = getNewTid(); // get a new TID
        if (tid == -1)
            return null;
        TCB tcb = new TCB(t, tid, pid); // create a new TCB
        Queue0.add(tcb);
        return tcb;
    }

    // A new feature added to p161
    // Removing the TCB of a terminating thread
    public boolean deleteThread() {
        TCB tcb = getMyTcb();
        if (tcb != null)
            return tcb.setTerminated();
        else
            return false;
    }

    public void sleepThread(int milliseconds) {
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    // A modified run of p161
    public void run() {
        Thread current = null;

        // this.setPriority( 6 );

        while (true) {
            try {
                // get the next TCB and its thrad
                if (Queue0.size() == 0 && Queue1.size() == 0 && Queue2.size() == 0)
                    continue;

                //For Queue 0:
                if (Queue0.size() != 0) {
                    TCB currentTCB = (TCB) Queue0.firstElement();
                    if (currentTCB.getTerminated() == true) {
                        Queue0.remove(currentTCB);
                        returnTid(currentTCB.getTid());
                        continue;
                    }

                    current = currentTCB.getThread();
                    if (current != null) {
                        if (current.isAlive())
                            // current.setPriority( 4 );
                            current.resume();
                        else {
                            // Spawn must be controlled by Scheduler
                            // Scheduler must start a new thread
                            current.start();
                            // current.setPriority( 4 );
                            run();
                        }
                    }

                    schedulerSleep();
                    // System.out.println("* * * Context Switch * * * ");

                    synchronized (Queue0) {
                        if (current != null && current.isAlive())
                            // current.setPriority( 2 );
                            current.suspend();
                    }
                    Queue0.remove(currentTCB);
                    Queue1.add(currentTCB);
                }
                //Same for Queue1:
                else if (Queue1.size() != 0) {
                    TCB currentTCB = (TCB) Queue1.firstElement();

                    if (currentTCB.getTerminated() == true) {
                        Queue1.remove(currentTCB);
                        returnTid(currentTCB.getTid());
                        continue;
                    }

                    current = currentTCB.getThread();
                    if (current != null) {
                        if (current.isAlive()) {
                            // current.setPriority( 4 );
                            current.resume();
                        } else {
                            // Spawn must be controlled by Scheduler
                            // Scheduler must start a new thread
                            current.start();

                            // current.setPriority( 4 );
                        }
                    }

                    schedulerSleep();
                    // System.out.println("* * * Context Switch * * * ");

                    synchronized (Queue1) {
                        if (current != null && current.isAlive()) {
                            // current.setPriority( 2 );
                            current.suspend();
                        }
                        Queue1.remove(currentTCB);
                        Queue2.add(currentTCB);
                    }
                }


                //Same for Queue2:
                else {
                    TCB currentTCB = (TCB) Queue2.firstElement();

                    if (currentTCB.getTerminated() == true) {
                        Queue2.remove(currentTCB);
                        returnTid(currentTCB.getTid());
                        continue;
                    }

                    current = currentTCB.getThread();
                    if (current != null) {
                        if (current.isAlive()) {
                            // current.setPriority( 4 );
                            current.resume();
                        } else {
                            // Spawn must be controlled by Scheduler
                            // Scheduler must start a new thread
                            current.start();
                            // current.setPriority( 4 );
                        }
                    }

                    schedulerSleep();
                    // System.out.println("* * * Context Switch * * * ");

                    synchronized (Queue2) {
                        if (current != null && current.isAlive()) {
                            // current.setPriority( 2 );
                            current.suspend();
                        }
                        Queue2.remove(currentTCB);
                        Queue2.add(currentTCB);
                    }
                }
            } catch (NullPointerException e3) {};
        }

    }
}