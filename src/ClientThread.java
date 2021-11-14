import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final Server server;
    BufferedReader br;
    OutputStreamWriter wr;

    public ClientThread(Server server, Socket socket){
        this.server = server;
        this.socket = socket;
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            wr = new OutputStreamWriter(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try{
            String name;
            String command = br.readLine();
            System.out.println("Server received: " + command);
            String[] commandSeparated = command.split("[:]");
            if(commandSeparated[0].equals("login")) {
                name = commandSeparated[1];
                server.putClient(name, socket);
            }
            while(true) {
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Message message = (Message) objectInputStream.readObject();
                Socket toSocket = server.getClients().get(message.messageTo());
                if(message.getMessage().equals("lista")){
                    Socket fromSocket = server.getClients().get(message.messageFrom());
                    OutputStream tos1 = fromSocket.getOutputStream();
                    OutputStreamWriter twr1 = new OutputStreamWriter(tos1);
                    twr1.write(Server.clients.keySet()+ "\r\n");
                    twr1.flush();
                }
                if(toSocket != null) {
                    OutputStream tos = toSocket.getOutputStream();
                    OutputStreamWriter twr = new OutputStreamWriter(tos);
                    if(message.getMessage().equals("lista"))
                        continue;
                    twr.write(message.messageFrom()+": "+message.getMessage() + "\r\n");
                    twr.flush();
                } else {
                    System.out.println("Client " + message.messageTo() + " does not exist");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}