package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {
    private Integer timeout;
    private boolean shutdown;
    private Integer limit;
    private long startTime;
    private OutputStream outPut;
    private InputStream inPut;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] buffer;
    private int data;
    

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
        this.startTime = 0;
        this.data = 0;
    }

    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {
        try {
            try (Socket clientSocket = new Socket(hostname, port)) {
                if (this.timeout != null) {
                    clientSocket.setSoTimeout(this.timeout);
                }

                if (toServerBytes != null) {
                    outPut = clientSocket.getOutputStream();
                    outPut.write(toServerBytes);
                    outPut.flush();
                }
                if (shutdown) {
                    clientSocket.shutdownOutput();
                }
                inPut = clientSocket.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();
                buffer = new byte[1024];

                this.startTime = System.currentTimeMillis();
                while (true) {
                    if (!((data = inPut.read(buffer)) == -1)) {
                        byteArrayOutputStream.write(buffer, 0, data);

                        if (limit != null && limit <= byteArrayOutputStream.size())
                            break;

                        if (timeout != null && System.currentTimeMillis() - startTime >= timeout) {
                            break;
                        }

                    } else
                        break;
                }

                clientSocket.close();
            }

        } catch (IOException e) {
            e.getMessage();

        }
        return byteArrayOutputStream.toByteArray();
    }
}
