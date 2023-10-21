package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        //Socket socket = null;
        try {
            Socket clientSocket = new Socket(hostname,port);
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            byte [] bufferData = new byte[1024];
            int data;

            ByteArrayOutputStream  buffer = new ByteArrayOutputStream ();
           // TCp Client
            output.write(toServerBytes);

            while (true){
                data =input.read(bufferData);
                if (data == -1)
                    break;
                buffer.write(bufferData,0,data);
            }
            return buffer.toByteArray();

        } catch (Exception e) {
            throw new IOException("error sending requests");
        }

    }
}
