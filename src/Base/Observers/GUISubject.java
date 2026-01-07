package Base.Observers;

public interface GUISubject {
    void addObserver(GUIObserver GUIObserver);
    void removeObserver(GUIObserver GUIObserver);
    void notifyObservers(int event);
}
