package hackathon.player;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DataReader<I, T> {

    void start() throws IOException;

    void registerReaderHandler(Function<I, T> dataHandler);

    void registerDataHandler(Consumer<T> dataHandler);

    void registerStopCallback(Consumer<Void> callback);
}
