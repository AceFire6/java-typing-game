import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Name: Jethro Muller
 * Student Number: MLLJET001
 */
public class WordController {
    private Score score;
    private WordPanel panel;
    private WordThread[] wordThreads;
    private WordRecord[] words;
    private JLabel[] labels;

    /**
     * Whether or not the game has finished.
     */
    private volatile boolean ended;
    /**
     * Whether or not the game is paused.
     */
    private volatile boolean paused;
    /**
     * Whether or not there has been a change.
     */
    private volatile boolean changed;
    /**
     * Whether or not the game is running.
     */
    private volatile boolean running;
    /**
     * The maximum number of words allowed to fall.
     */
    private int maxWords;


    /**
     * Parametrized constructor
     *
     * @param maxWords The maximum number of words allowed to fall.
     * @param score    The score object that keeps the scores.
     * @param words    WordRecord[] that holds all the words on screen.
     */
    public WordController(int maxWords, Score score, WordRecord[] words) {
        super();
        this.score = score;
        this.words = words;
        this.wordThreads = new WordThread[words.length];
        this.maxWords = maxWords;
        ended = true;
        paused = false;
        running = false;
    }

    /**
     * Updates the score labels on the WordPanel with the updated scores.
     */
    public synchronized void updateScoreLabels() {
        labels[0].setText("Caught: " + score.getCaught() + "    ");
        labels[1].setText("Missed:" + score.getMissed() + "    ");
        labels[2].setText("Score:" + score.getScore() + "    ");
        labels[3].setText("Incorrect Words:" + score.getIncorrectWords() + "    ");
        setChanged();
    }

    /**
     * Checks the word to see if it matches the provided text.
     *
     * @param text The text to check against.
     * @return boolean Whether or not the word matched.
     */
    public synchronized boolean checkWord(String text) {
        Arrays.sort(words, new Comparator<WordRecord>() {
            @Override
            public int compare(WordRecord o1, WordRecord o2) {
                if (o1.equals(o2)) {
                    return 0;
                }
                if (o1.getY() > o2.getY()) {
                    return -1;
                }
                return 1;
            }
        });

        for (WordRecord word : words) {
            if (word.matchWord(text)) {
                score.caughtWord(text.length());
                updateScoreLabels();
                if (score.getCaught() >= maxWords) {
                    winGame();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Creates WordThread objects to manipulate the WordRecords in separate threads.
     */
    public void startWords() {
        ended = false;
        running = true;
        int index = 0;
        for (WordRecord word : words) {
            wordThreads[index] = new WordThread(word, this);
            new Thread(wordThreads[index]).start();
            index++;
        }
    }

    /**
     * Increments the missed word count and detects if it's gone over the limit.
     */
    public synchronized void missedWord() {
        score.missedWord();
        updateScoreLabels();
        if (score.getMissed() >= 10) {
            stopGame();
            setChanged();
            JOptionPane.showMessageDialog(panel, "Game Over!\n" +
                                                 "Your score was: " + score.getScore() +
                                                 "\nYou caught " + score.getCaught() + " word(s)." +
                                                 "\nYou missed " + score.getMissed() + " word(s)." +
                                                 "\nYou typed " + score.getIncorrectWords() +
                                                 " word(s) incorrectly");

            resetScore();
            panel.repaintOnce();
        }
    }

    /**
     * Resets the scores in the Score object.
     */
    public void resetScore() {
        score.resetScore();
        updateScoreLabels();
    }

    /**
     * Adds the JLabel objects to this.
     *
     * @param labels The JLabels to be added to this.
     */
    public void addLabels(JLabel[] labels) {
        this.labels = labels;
    }

    /**
     * Adds the JPanel to this.
     *
     * @param panel JPanel to be added to this.
     */
    public void addPanel(WordPanel panel) {
        this.panel = panel;
    }

    /**
     * Stops the game. Stops all threads and sets the appropriate flags.
     */
    public void stopGame() {
        for (WordThread wordThread : wordThreads) {
            wordThread.stop();
        }
        ended = true;
        running = false;
    }

    /**
     * Ends the game. By stopping the game, reseting the score and reseting the display.
     */
    public void endGame() {
        stopGame();
        resetScore();
        panel.repaintOnce();
    }

    /**
     * @return boolean indicating whether or not the game has ended.
     */
    public boolean ended() {
        return ended;
    }

    /**
     * Runs if the win condition is met.
     * Stops the game and displays a message.
     */
    public void winGame() {
        stopGame();
        setChanged();
        JOptionPane.showMessageDialog(panel, "You've won!\n" +
                                             "Your score was: " + score.getScore() +
                                             "\nYou caught " + score.getCaught() + " word(s)." +
                                             "\nYou missed " + score.getMissed() + " word(s)." +
                                             "\nYou typed " + score.getIncorrectWords() +
                                             " word(s) incorrectly");
        resetScore();
        panel.repaintOnce();
    }

    /**
     * Sets the paused flag to the opposite of whatever it currently is.
     */
    public void setPaused() {
        paused = !paused;
    }

    /**
     * @return boolean indicating whether or not the game is paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @return boolean indicating if the game's model has changed.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets the changed flag to false.
     */
    public synchronized void setUnchanged() {
        changed = false;
    }

    /**
     * Sets the changed flag to true.
     */
    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * @return boolean indicating if the game is running.
     */
    public boolean isRunning() {
        return running;
    }
}
