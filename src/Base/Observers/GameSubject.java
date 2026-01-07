package Base.Observers;

public interface GameSubject {
    void addObserver(GameObserver Observer);
    void notifyObservers(int event, Object context);
}
