/**
 * @author LinusW
 * @create 2020-11-10-5:47 PM
 */
import java.util.Vector;

public class QueueNode {
    private Vector<Integer> queue;
    public QueueNode(){
        queue = new Vector<Integer>();
    }
    //Let the thread sleep until wake()
    public synchronized int sleep() {
        try {
            //Wait until somewhere calls the notify() (by the time we call wake())
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Removing the tid
        return queue.remove(0);
    }
    //To wake up the thread at the queue top
    public synchronized void wake(int tid){
        //Adding the thread that is wanted to be wake up
        queue.add(tid);
        //Find the thread which is waiting
        notify();
    }
}