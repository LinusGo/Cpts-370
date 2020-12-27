import java.net.*;
import java.io.*;
import java.util.*;

public class CmdServer extends Thread {

    //Total number of ports (5500 - 5000)
    private final int TOTAL_PORTS = 501; 
    
    //Checks the message received, decides if the server disconnects the connection.
    private boolean disconnected(ServerSocket serverSocket, ServerSocket socket) throws Exception {
        Socket sock = serverSocket.accept();
        PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
        BufferedReader in =
                new BufferedReader(new InputStreamReader(sock.getInputStream()));
        String cmd;
        while ((cmd = in.readLine()) != null) {
            // "bye" command
            if (cmd.startsWith("bye")) {
                sock.close();
                serverSocket.close();
                break;
            } 
            // "die" command
            else if (cmd.startsWith("die")) {
                sock.close();
                serverSocket.close();
                socket.close();
                return true; // The server disconnects and exit
            } 
            // "whoami,ls,pwd,ps,man,echo,date" command
            else if (cmd.matches("(whoami|ls|pwd|ps|man|echo|date).*")) { 
                Process process = Runtime.getRuntime().exec(cmd);
                BufferedReader readLine = new
                        BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = readLine.readLine()) != null) {
                    pout.println(line);
                }
                pout.println("Finished");
            }
            // In this case, no command matched. Reverse the text and send back to client.
             else {
                pout.println(new StringBuilder(cmd).reverse().toString());
            }
        }
        return false;
    }

    public void run() {
        try {
            // Let the server loop through ports in the range 5000 - 5500
            ServerSocket socket = null;
            for (int i = 0; i <=TOTAL_PORTS; i++) {
                try {
                    socket = new ServerSocket(i + 5000);
                    break;
                } catch (Exception ioe) {
                    continue;
                }
            }
            // Print out the listening port message
            SysLib.cout("sigint.eecs.wsu.edu is listening on port " + socket.getLocalPort() + "\n");
            Process process = Runtime.getRuntime().exec("whoami");
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String username = bufferedReader.readLine();
            while (true) {
                Socket sock = socket.accept();
                PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String myUsername = in.readLine();
                // Check its first reveiced message against its own username
                if (myUsername.equals(username)) {
                    // Opens a new random port
                    ServerSocket serverSocket = new ServerSocket(0);
                    pout.println(serverSocket.getLocalPort());
                    sock.close();
                    // If the client calls "die", the server disconnects and exit
                    if (disconnected(serverSocket, socket)){
                        break;
                    }
                } else {
                    sock.close();
                }
            }
        } catch (Exception ioe) {
            System.err.println(ioe);
            SysLib.exit();
        }
        SysLib.exit();
    }
}
