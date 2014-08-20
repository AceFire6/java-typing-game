import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jethro Muller on 2014/08/20.
 * Threads the WordRecord objects to allow for concurrent access and movement.
 */
public class WordThread extends Thread {
    private WordRecord word;
    private WordPanel panel;

    public WordThread(WordRecord word, WordPanel wordPanel) {
        super();
        this.panel = wordPanel;
        this.word = word;
        setEvent();
    }

    private void setEvent() {
        Timer dropTimer = new Timer();
        TimerTask timedDrop = new TimerTask() {
            @Override
            public void run() {
                word.drop(1);
                panel.repaint();
                setEvent();
            }
        };
        dropTimer.schedule(timedDrop, word.getSpeed());
    }
}
