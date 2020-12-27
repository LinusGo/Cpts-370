/**
 * @author LinusW
 * @create 2020-11-10-7:36 PM
 */

public class SyncQueue {
    private QueueNode [] queues;
    private static int DEFAULTTID = 0;
    private static final int DEFAULTCOND = 10;

    public SyncQueue(){
        queues = new QueueNode[DEFAULTCOND];
        for (int i = 0 ; i < DEFAULTCOND; i++){// to initilize the array
            queues[i] = new QueueNode();
        }
    }
    //Initializes the SyncQueue with max condition
    public SyncQueue(int condMax){
        queues = new QueueNode[condMax];
        for (int i = 0 ; i < condMax ; i++){
            queues[i] = new QueueNode();
        }
    }
    //Putting the thread to sleep in satisfied condition by calling the sleep()
    //If the condition is outside of the array size, give an error.
    public int enqueueAndSleep(int condition) {
        if (condition > -1 && condition < this.queues.length){
            return queues[condition].sleep();
        }
        else{
            return -1;// Error
        }
    }
    //starts the thread at the queue top by calling the wake(int tid)
    public void dequeueAndWakeup(int condition){
        if (condition > -1 && condition < queues.length){
            queues[condition].wake(DEFAULTTID);
        }
    }
    //starts the thread with the given tid by calling the wake(int tid)
    public void dequeueAndWakeup(int condition, int tid){
        if (condition > -1 && condition < queues.length){
            queues[condition].wake(tid);
        }
    }
}