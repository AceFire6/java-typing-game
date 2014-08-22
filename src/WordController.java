import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;


public class WordController {
    private Score score;
    private WordPanel panel;
    private WordThread[] wordThreads;
    private WordRecord[] words;
    private JLabel[] labels;

    private boolean ended;
    private int maxWords;
    private int wordsDone;


    public WordController(int maxWords, Score score, WordRecord[] words) {
        super();
        this.score = score;
        this.words = words;
        this.wordThreads = new WordThread[words.length];
        this.maxWords = maxWords;
        wordsDone = 0;
    }

    public void updateScoreLabels() {
        labels[0].setText("Caught: " + score.getCaught() + "    ");
        labels[1].setText("Missed:" + score.getMissed() + "    ");
        labels[2].setText("Score:" + score.getScore() + "    ");
    }

    public void checkWord(String text) {
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
                wordsDone++;
                break;
            }
        }
        if (wordsDone >= maxWords) {
            winGame();
        }
    }

    public void startWords() {
        ended = false;
        int index = 0;
        for (WordRecord word : words) {
            wordThreads[index] = new WordThread(word, this);
            new Thread(wordThreads[index]).start();
            index++;
        }
    }

    public void missedWord() {
        score.missedWord();
        wordsDone++;
        if ((score.getMissed() > 10) || (wordsDone >= maxWords)) {
            stopGame();
        }
    }

    public void refreshGUI() {
        updateScoreLabels();
        panel.repaint();
    }

    public void resetScore() {
        score.resetScore();
        updateScoreLabels();
    }

    public void addLabels(JLabel[] labels) {
        this.labels = labels;
    }

    public void addPanel(WordPanel panel) {
        this.panel = panel;
    }

    public void stopGame() {
        for (WordThread wordThread : wordThreads) {
            wordThread.stop();
            ended = true;
            wordsDone = 0;
        }
        refreshGUI();
    }

    public boolean ended() {
        return ended;
    }

    public void winGame() {
        stopGame();
        JOptionPane.showMessageDialog(panel, "You've won!");
    }
}
