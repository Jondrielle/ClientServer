import java.net.*;
import java.io.*;
public class MyClient extends Thread {
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public MyClient(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
            String message = "";
            System.out.println("Enter the username: ");
            while (!message.equals("Bye")) {
                try {
                    message = input.readLine();

                    if (message.isEmpty() == true)
                        break;
                    out.writeUTF(message);
                    out.writeUTF(input.readLine());
                    //out.writeUTF(message);
                    //System.out.println(input.readLine());
                } catch (IOException i) {
                    System.out.println(i);
                }
            }
            try {
                input.close();
                out.close();
                socket.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        MyClient client = new MyClient("localhost",4000);
    }
}
