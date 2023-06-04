package hackathon.player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class BufferedFileDataReaderImpl<T> implements DataReader<String, T> {

    private final String inputFile;
    private volatile Function<String, T> readerHandler = null;
    private volatile Consumer<T> dataHandler = null;
    private volatile Consumer<Void> stopCallback = null;

    public BufferedFileDataReaderImpl(String inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public void start() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            bufferedReader.lines().forEach(s -> dataHandler.accept(readerHandler.apply(s)));
        }
    }

    @Override
    public synchronized void registerReaderHandler(Function<String, T> dataHandler) {
        if (this.readerHandler == null) {
            this.readerHandler = dataHandler;
        } else {
            System.err.println("Reader Handler already registered");
        }
    }

    @Override
    public synchronized void registerDataHandler(Consumer<T> dataHandler) {
        if (this.dataHandler == null) {
            this.dataHandler = dataHandler;
        } else {
            System.err.println("Data Handler already registered");
        }
    }

    @Override
    public synchronized void registerStopCallback(Consumer<Void> callback) {
        if(stopCallback == null) {
            this.stopCallback = callback;
        } else {
            System.err.println("Stop Callback already registered");
        }

    }
}
