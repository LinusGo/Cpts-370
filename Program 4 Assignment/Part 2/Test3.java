/**
 * @author LinusW
 * @create 2020-11-12-12:10 AM
 */

import java.util.Date;
import java.util.*;
public class Test3 extends Thread {
    private int thread;

    public Test3(String[] args) {
        thread = Integer.parseInt(args[0]);
    }

    public void run() {
        //Record the staring time of the test
        long initialTime = new Date().getTime();

        String[] thread_A = SysLib.stringToArgs("TestThread3a");
        String[] thread_B = SysLib.stringToArgs("TestThread3b");
        //Start both threads
        for (int i = 0; i < thread; i++) {
            SysLib.exec(thread_A);
            SysLib.exec(thread_B);
        }
        //Wait for the two running threads and join
        for (int i = 0; i < (thread * 2); i++) {
            SysLib.join();
        }
        //Record the final run time of the test
        long finalTime = new Date().getTime();
        //Print elapsed time
        SysLib.cout("Elapsed time: " + (finalTime - initialTime) + " ms\n");
        SysLib.exit();
    }

}