import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MyRunnable implements Runnable{

private Socket socket;
  public MyRunnable(Socket socket) {
    this.socket = socket;

}

byte[] byteServer = new byte[0];
String hostname = null;
int clientsPort = 0;
boolean shutdown = false;
Integer timeout = null;
Integer limit = null;


@Override
public void run() {
    try {
        OutputStream output = socket.getOutputStream();
        StringBuilder response = new StringBuilder();

        byte[] buffer = new byte[1024];
        socket.getInputStream().read(buffer);
        String outputString = new String(buffer, StandardCharsets.UTF_8);
        String[] lines = outputString.split("\n");
        System.out.println(lines[0].trim());
        System.out.println(lines[1].trim());

        String[] requestLines = outputString.split("\r\n");
        System.out.println(outputString);
        if (requestLines.length == 0 || !requestLines[0].startsWith("GET")
                || !requestLines[0].endsWith("HTTP/1.1")) {
            String resp = "HTTP/1.1 " + "400" + "Bad Request" + "\r\n\r\n";
                output.write(resp.getBytes());
                return;
        }

        String myURL = requestLines[0].split(" ")[1];
        String[] param = myURL.split("\\?");

        if (param.length > 0 && param[0].equals("/ask")) {
            if (param.length < 2) {
                 String resp = "HTTP/1.1 " + "400" + "Bad Request" + "\r\n\r\n";
                 output.write(resp.getBytes());
                return;
            }

            String[] ListArray = param[1].split("&");
           
             for (int i = 0; i < ListArray.length; i++) {
                   String[] ArrayValue = ListArray[i].split("=");

                    if (!(ArrayValue.length < 2)) {
                        if (ArrayValue[0].equals("shutdown"))
                            shutdown = Boolean.parseBoolean(ArrayValue[1]);
                        else if (ArrayValue[0].equals("timeout"))
                            timeout = Integer.parseInt(ArrayValue[1]);
                        else if (ArrayValue[0].equals("limit"))
                            limit = Integer.parseInt(ArrayValue[1]);
                        else if (ArrayValue[0].equals("hostname"))
                            hostname = ArrayValue[1];
                        else if (ArrayValue[0].equals("port"))
                            clientsPort = Integer.parseInt(ArrayValue[1]);
                        else if (ArrayValue[0].equals("string"))
                            byteServer = ArrayValue[1].getBytes();
                    } else {
                        String resp = "HTTP/1.1 " + "400" + "Bad Request" + "\r\n\r\n";
                 output.write(resp.getBytes());
                return;
                    }

                    if (ArrayValue.length < 2) {
                        String resp = "HTTP/1.1 " + "400" + "Bad Request" + "\r\n\r\n";
                        output.write(resp.getBytes());
                   return;
                   }

                }

            if (hostname == null || clientsPort == 0) {
                String resp = "HTTP/1.1 " + "400" + "Bad Request" + "\r\n\r\n";
                output.write(resp.getBytes());
                return;
            }

            try {
                response.append("HTTP/1.1 200 OK\r\n\r\n");

                TCPClient client = new TCPClient(shutdown, timeout, limit);
                response.append(new String(client.askServer(hostname, clientsPort, byteServer)));

            } catch (Exception e) {
                String resp = "HTTP/1.1 " + "500" + "Server error" + "\r\n\r\n";
                output.write(resp.getBytes());
                return;
            }
        
        }
        else {
            String resp = "HTTP/1.1 " + "404" + "Error: Not Found" + "\r\n\r\n";
            output.write(resp.getBytes());
                return;
        }

        output.write(response.toString().getBytes());
        socket.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

}
}