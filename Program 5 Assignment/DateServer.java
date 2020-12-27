import java.net.*;
import java.io.*;
import java.util.*;

public class DateServer extends Thread {

    public DateServer() {
    }

    public void run() {
        try {
            ServerSocket sock = new ServerSocket(0);
            SysLib.cout("Listening on port " + sock.getLocalPort() + "\n");
            /* now listen for connections */
            while (true) {
                Socket client = sock.accept();
                PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
                /* write the Date to the socket */
                pout.println(new java.util.Date().toString());
                /* close the socket and resume */
                /* listening for connections */
                client.close();
            }
        } catch (IOException ioe) {
            System.err.println(ioe + "\n");
            SysLib.exit();
        }
    }
}
