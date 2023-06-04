package hackathon.player;

public interface OutputController<T> {
    void receive(T element);

    void stop();
}
