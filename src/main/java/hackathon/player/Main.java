package hackathon.player;

import hackathon.player.model.MarketData;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;

/**
 * 1. Read file
 * 2. Adjust Data.
 * 3. Sinking Control
 * 4. Sink on TCP
 */
public class Main {

    private final BlockingQueue<MarketData> queue;
    private volatile boolean keepRunning = true;
    private final OutputController<MarketData> outputController;

    public Main(int listeningPort, double speedUp) throws IOException {
        queue = new ArrayBlockingQueue<>(8096);//TODO Make this configurable;
        outputController = new TCPControllerImpl(listeningPort, speedUp);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                while (keepRunning) {
                    MarketData md = queue.take();
                    outputController.receive(md);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void readFile(String inputFile) throws IOException {
        DataReader<String, MarketData> reader = new BufferedFileDataReaderImpl<>(inputFile);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.S");
        reader.registerReaderHandler(s -> {

            String[] tuples = s.split(",");
            try {
                return new MarketData(tuples[0], Integer.parseInt(tuples[1]), Integer.parseInt(tuples[2]), Long.parseLong(tuples[3]), Integer.parseInt(tuples[4]), Integer.parseInt(tuples[5]),
                        Integer.parseInt(tuples[6]), Integer.parseInt(tuples[7]), Long.parseLong(tuples[8]), sdf.parse(tuples[9]), Long.parseLong(tuples[10]), Long.parseLong(tuples[11]));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        reader.registerDataHandler(e -> {
            try {
                queue.put(e);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        reader.start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage java Main <inputFileName> <Port>");
            System.exit(1);
        }
        Main main = new Main(Integer.parseInt(args[1]), 1.0);
        Runtime.getRuntime().addShutdownHook(new Thread(main::stop));
        main.readFile(args[0]);
    }

    private void stop() {
        keepRunning = false;
        outputController.stop();
    }
}
