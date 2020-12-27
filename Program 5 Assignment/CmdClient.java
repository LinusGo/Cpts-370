import java.net.*;
import java.io.*;

public class CmdClient extends Thread {

    private final int portNumber;
    private String hostName;
    private final String[] args;

    public CmdClient() {
        this.args = new String[0];
        portNumber = Integer.parseInt(args[0]);
    }

    public CmdClient(String[] args) {
        this.args = new String[0];
        portNumber = Integer.parseInt(args[0]);
        // set hostname equal to passed parameter
        if (args.length != 1 && args.length != 2) {
            hostName = args[1];
        } 
        // Checking if port number parameter is out of range
        else if (Integer.parseInt(args[0]) < 5000 || Integer.parseInt(args[0]) > 5500) {
            SysLib.cerr("Port Number out of range (5000-5500");
            SysLib.exit();
        } 
        // Host name default to "localhost"
        else {
            hostName = "localhost";
        }
    }


    public void run() {
        try {
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader bin =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String username = bin.readLine();
            /* make connection to server socket */
            Socket sock = new Socket(hostName, portNumber);
            PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            /* read the date from the socket */
            String line;
            pout.println(username);
            if ((line = in.readLine()) != null) {
                sock.close();
                sock = new Socket(hostName, Integer.parseInt(line));
                pout.close();
                in.close();
                pout = new PrintWriter(sock.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            }
            else
                SysLib.exit();
            while (true) {
                StringBuffer stringBuffer = new StringBuffer();
                SysLib.cin(stringBuffer);
                String cmd = stringBuffer.toString();
                pout.println(cmd);
                // Check for cmd with commands
                if (cmd.matches("(whoami|ls|pwd|ps|man|echo|date).*")) {
                    while (true) {
                        line = in.readLine();
                        if (line.equals("Finished"))
                            break;
                        SysLib.cout(line + "\n");
                    }
                } else if ((line = in.readLine()) != null) {
                    SysLib.cout(line + "\n");
                } 
                // In this case, close the socket connection 
                else if (cmd.matches("(bye|die).*")) {
                    break;
                } 
            }
        }
        catch (IOException ioe) {
            SysLib.cerr(ioe.toString() + "\n");
            SysLib.exit();
        }
        SysLib.exit();
    }
}
