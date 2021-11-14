import java.io.*;
import java.net.Socket;

class Message implements Serializable {
    private final String messageFrom;
    private final String messageTo;
    private final String message;

    public Message(String messageFrom, String messageTo, String message) {
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String messageFrom() {
        return messageFrom;
    }

    public String messageTo() {
        return messageTo;
    }
}
public class Client {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();
        String writeTo = br.readLine();
        String serverHost = "127.0.0.1";
        int serverPort = 4444;

        try {
            Socket socket = new Socket(serverHost, serverPort);
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter wr = new OutputStreamWriter(os);
            wr.write("login:" + name + "\r\n");
            wr.flush();
            ClientWriter cw = new ClientWriter(br);
            cw.start();
            br = new BufferedReader(new InputStreamReader(System.in));
            while(true)
            {
                String temp = br.readLine();
                Message message = new Message(name,writeTo,temp);
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
                if(temp.equals("kraj"))
                    break;
            }
        }
        catch(IOException ex){
            System.err.println("Error Connection!");
            ex.printStackTrace();
        }
    }
}