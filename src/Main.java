import http.KVServer;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        KVServer kvServer;
        try {
            kvServer = new KVServer("localhost", 8080);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();


        kvServer.stop();
    }
}
