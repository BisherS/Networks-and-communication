import java.net.*;
import java.io.*;

public class ConcHTTPAsk {
    public static void main(String[] args) throws IOException {
        try {
            int port = Integer.parseInt(args[0]);
            ServerSocket ServerSocket = new ServerSocket(port);
           
            while (true) {
                Socket socket = ServerSocket.accept();
                MyRunnable run = new MyRunnable(socket);
               
                Thread ThreadTest = new Thread(run);
                ThreadTest.start();
            }
        } catch (Exception ee) {
            System.out.println("an error occurs " + ee);
        }
    }
}
