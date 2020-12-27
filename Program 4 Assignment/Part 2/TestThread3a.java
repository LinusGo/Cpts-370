/**
 * @author LinusW
 * @create 2020-11-12-6:40 PM
 */
public class TestThread3a extends Thread{
    public TestThread3a() {
    }

    public void run() {
        //An simulate computation that puts pressure on the CPU
        //Looping the sum to a astronomical number
        double sum = 0.0;
        for (int i = 0; i < 20000000; i++) {
            sum += Math.pow(Math.sqrt(i),10);
        }
        SysLib.cout("Compute Thread Done!\n");
        SysLib.exit(); 
    }
}

