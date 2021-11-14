import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread{

    private static final int portNumber = 4444;

    private int serverPort;

    public static HashMap<String, Socket> clients;

    public HashMap<String, Socket> getClients(){
        return clients;
    }
    public Server(int portNumber){
        this.serverPort = portNumber;
    }
    public Server(){
    }
    public static void main(String[] args) {
        Server thread = new Server();
        thread.start();
        Server server = new Server(portNumber);
        server.startServer();
    }
    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            try {
                String temp = br.readLine();
                if(temp.equals("lista")){
                    System.out.println(clients);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void putClient(String name, Socket socket) {
        try {
            clients.put(name, socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
            } catch (IOException ex){
                System.out.println("Accept failed on : " + serverPort);
            }
        }
    }

    private void startServer(){
        clients = new HashMap<>();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+serverPort);
            System.exit(1);
        }
    }

}