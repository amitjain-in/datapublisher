package hackathon.player.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestClient {

    private final MarketDataParser parser;
    private volatile boolean keepListening = true;

    public TestClient() {
        this.parser = new MarketDataParser();
    }

    public static void main(String[] args) throws IOException {
        //connect to socket
        //start subscription
        //print market-data
        TestClient testClient = new TestClient();
        testClient.connect(args[0], Integer.parseInt(args[1]));
        Runtime.getRuntime().addShutdownHook(new Thread(testClient::stop));
    }

    private void connect(String ipAddress, int port) throws IOException {

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ipAddress, port));
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            byte[] one = new byte[1];
            socket.getOutputStream().write(one);

            while(keepListening) {
                byte[] bytes = new byte[130];
                inputStream.read(bytes, 0, bytes.length);
                System.out.println(this.parser.parse(ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)));
            }
        }
    }

    public void stop() {
        keepListening = false;
    }
}
