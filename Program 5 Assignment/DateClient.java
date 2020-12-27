import java.net.*;
import java.io.*;

public class DateClient extends Thread {

    private final String[] args;

    public DateClient() {
        this.args = new String[0];
    }

    public DateClient(String[] args) {
        this.args = args;
    }

    public void run() {
        try {
            if (args.length == 0) {
                SysLib.cerr("Port number parameter needed(5000 - 5500) + \n");
                SysLib.exit();
            } else {
                //The port number parameter (range 5000 - 5500)
                int portNumber = Integer.parseInt(args[0]);
                /* make connection to server socket */
                Socket sock = new Socket("localhost", portNumber);//I found this more readable
                InputStream in = sock.getInputStream();
                BufferedReader bin = new
                        BufferedReader(new InputStreamReader(in));
                /* read the date from the socket */
                String line;
                while ((line = bin.readLine()) != null)
                    SysLib.cout(line + "\n");
                /* close the socket connection */
                sock.close();
                SysLib.exit();
            }
        } catch (IOException ioe) {
            System.err.println(ioe + "\n");
            SysLib.exit();
        }
    }
}
