import java.io.BufferedReader;

public class ClientWriter extends Thread {
    BufferedReader br;
    public ClientWriter(BufferedReader br) {
        this.br = br;
    }

    public void run() {
        try {
            while(true) {
                String received = br.readLine();
                System.out.println(received);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
