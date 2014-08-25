/**
 * Created by Jethro Muller on 2014/08/20.
 * Threads the WordRecord objects to allow for concurrent access and movement.
 */
public class WordThread implements Runnable {
    /**
     * The word this thread manages.
     */
    private WordRecord word;
    private WordController wordController;

    /**
     * Parametrized constructor.
     *
     * @param word           WordRecord oject
     * @param wordController WordController object that handles various inter-clsss calls.
     */
    public WordThread(WordRecord word, WordController wordController) {
        super();
        this.word = word;
        this.wordController = wordController;
    }

    /**
     * Resets the current word.
     */
    public synchronized void stop() {
        word.resetWord();
    }

    /**
     * The main loop of the Thread.
     * Runs in parallel when .start() is called.
     */
    @Override
    public void run() {
        //If the game is still being played
        while (!wordController.ended()) {
            // if the word was missed.
            if (word.missed()) {
                wordController.missedWord();
                word.resetWord();
                wordController.setChanged();
                //If the game is paused, don't do anything.
            } else if (wordController.isPaused()) {
                continue;
                //The word wasn't missed and the game isn't paused
                //Drop the word and update the scores.
            } else {
                word.drop(1);
                wordController.updateScoreLabels();
            }
            try {
                //Waits until the next movement time.
                Thread.sleep(word.getSpeed() / 15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
