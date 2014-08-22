/**
 * Created by Jethro Muller on 2014/08/20.
 * Threads the WordRecord objects to allow for concurrent access and movement.
 */
public class WordThread implements Runnable {
    private WordRecord word;
    private WordController wordController;

    public WordThread(WordRecord word, WordController wordController) {
        super();
        this.word = word;
        this.wordController = wordController;
    }

    public void stop() {
        word.resetWord();
    }

    @Override
    public void run() {
        while (!wordController.ended()) {
            if (word.missed()) {
                wordController.missedWord();
                word.resetWord();
            } else {
                word.drop(1);
            }
            wordController.refreshGUI();
            try {
                Thread.sleep(word.getSpeed()/15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
