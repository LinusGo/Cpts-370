import java.util.HashSet;
/**
 * @author LinusW
 * @create 2020-10-01-10:02 PM
 */

public class Shell extends Thread
{
    String shellName = "Shell";
    public Shell() {
        //Constructor of Shell
    }
    public void run()
    {
        int cmdNum = 0;
        while(true)//Getting input from the user before the program is exited
        {
            cmdNum ++;
            SysLib.cout( shellName + "[" + cmdNum + "]% " );//adding 1 to the command number and display this integer
            StringBuffer buf = new StringBuffer();
            SysLib.cin(buf);
            String stringCommands = new String(buf);
            if(stringCommands.compareTo("exit") == 0){//Check for exit before next command
                SysLib.cout("exit");
                SysLib.exit();
                break;
            }
            if(!stringCommands.isEmpty())
            {
                for(String inputCommands : stringCommands.split(";"))//Traverse the input commands and determine by delimiter
                {
                    execution(inputCommands);
                }
            }
        }
    }
    public void execution(String inputString)
    {
        HashSet<Integer> integers = new HashSet<Integer>();//Keep track of the child thread id
        int child;
        for(String inputCommands : inputString.split("&")) {
            String[] commandString = SysLib.stringToArgs(inputCommands);
            if((child = SysLib.exec(commandString)) != -1){//load args, check the success of returning child thread ID
                SysLib.cout(commandString[0] + "\n");
                integers.add(child);
            }
        }
        while(!integers.isEmpty())//join or give an err after child thread complete
        {
            if(((child = SysLib.join()) != -1) && (integers.contains(child))){
                integers.remove(child);
            }
            else {
                SysLib.cerr("Error -Join-\n");
            }
        }
    }
}