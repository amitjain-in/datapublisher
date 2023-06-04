package hackathon.player;

import hackathon.player.model.MarketData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPControllerImpl implements OutputController<MarketData> {

    private final List<Socket> clients = new LinkedList<>();

    private volatile boolean keepListening = true;
    private MarketData lastPublished = null;
    private Date lastPublishedTime = new Date();
    private final double speedUp;
    private final boolean debug;

    private final ServerSocket serverSocket;

    public TCPControllerImpl(int port, double speedUp) throws IOException {
        this.speedUp = speedUp;
        String debugStr = System.getProperties().getProperty("debug");
        debug = debugStr != null && debugStr.trim().equalsIgnoreCase("true");
        serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (keepListening) {
                try {
                    Socket client = serverSocket.accept();
                    client.getInputStream().readNBytes(1);
                    System.out.println("Client added from " + client.getInetAddress());
                    clients.add(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void receive(MarketData element) {
        List<Socket> deadClients = new LinkedList<>();
        long expectedDelay = lastPublished != null ? element.getTimestamp().getTime() - lastPublished.getTimestamp().getTime() : 0;
        //noinspection StatementWithEmptyBody
        while (expectedDelay > speedUp * (new Date().getTime() - lastPublishedTime.getTime())) ;
        if (debug) {
            System.out.println("Publishing " + element);
        }
        clients.forEach(socket -> {
            try {

                socket.getOutputStream().write(element.toBytes());
            } catch (IOException e) {
                System.err.println("Client disconnected " + socket.getInetAddress());
                deadClients.add(socket);
                throw new RuntimeException(e);
            }
        });
        if (!deadClients.isEmpty()) {
            clients.removeAll(deadClients);
        }
        lastPublished = element;
        lastPublishedTime = new Date();
    }

    @Override
    public void stop() {
        keepListening = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
