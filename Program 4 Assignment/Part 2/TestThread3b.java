/**
 * @author LinusW
 * @create 2020-11-12-7:30 PM
 */
public class TestThread3b extends Thread{
    public TestThread3b() {
    }

    public void run() {
        byte[] buffer = new byte[512];
        //Simulating the read and write onto the disk
        //Looping through 500 blocks, write and read
        for (int i = 0; i < 500; i++) {
            SysLib.rawwrite(i, buffer);
            SysLib.rawread(i, buffer);
        }
        SysLib.cout("Disk Thread Done!\n");
        SysLib.exit();
    }
}
