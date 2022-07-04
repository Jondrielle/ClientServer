import org.w3c.dom.ls.LSOutput;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MyServer{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private ArrayList<ClientHandler> handler = new ArrayList<>();


    public MyServer(int port) {
        try {
            server = new ServerSocket(port);
            while(true) {
                socket = server.accept();
                ClientHandler clientHandler = new ClientHandler(socket,handler);
                handler.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            }

    public static class ClientHandler extends Thread {
        private static Socket socket;
        private DataInputStream in;
        private PrintWriter out;
        private String userName = null;
        private String line = "";
        public static ArrayList<ClientHandler> threadList;

        public ClientHandler(Socket socket,ArrayList<ClientHandler> clientThreads) {
            this.socket = socket;
            this.threadList = clientThreads;
        }

        @Override
        public void run() {
            try {
               // out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
                out = new PrintWriter(socket.getOutputStream());
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                while (!line.equals("Bye")) {
                    // try {
                   // System.out.println("In while loop for line");
                    line = in.readUTF();
                    clientMessages();
                }
                System.out.println(userName + " disconnected with a Bye message");
                System.out.println("Server: Goodbye " + userName);
                threadList.remove(userName);
                socket.close();
                in.close();
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            }
        }

        private static void printThreadList() {
            System.out.println("AllUsers:");
            for (int i = 0; i < threadList.size(); i++) {
                int j = i + 1;
                System.out.println(j + ") " + threadList.get(i));
            }
        }

        private void broadcastMessage(String message){
            for (ClientHandler client: threadList) {
                client.out.println(message);
                //System.out.println("OUTSIDE FOR LOOP IN BROADCAST METHOD");
            }
        }
        private void clientMessages(){
            if (line.equals("AllUsers")) {
                printThreadList();
            }
            if(userName == null){
                userName = line;
                System.out.println("Welcome: " + userName );
                String greeting = "Welcome: " + userName + "\n";
                broadcastMessage(greeting);
            } else {
                System.out.println(userName + ": " + line);
                String clientMessage = userName + ": " + line;
                broadcastMessage(clientMessage);
            }

        }
    }

    public static void main(String[] args) throws IOException {
        MyServer server = new MyServer(4000);
    }
}


