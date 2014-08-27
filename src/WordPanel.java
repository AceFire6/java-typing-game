import javax.swing.*;
import java.awt.*;

public class WordPanel extends JPanel implements Runnable {
    private WordRecord[] words;
    private int noWords;
    private int maxY;
    private WordController wordController;


    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.clearRect(0, 0, width, height);
        g.setColor(Color.red);
        g.fillRect(0, maxY - 10, width, height);

        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 26));
        //draw the words
        //animation must be added
        for (int i = 0; i < noWords; i++) {
            // Some words stuck out.
            g.drawString(words[i].getWord(), words[i].getX(), words[i].getY() - 5);
        }

    }

    public WordPanel(WordRecord[] words, int maxY, WordController wordController) {
        this.words = words; //will this work?
        noWords = words.length;
        this.maxY = maxY;
        this.wordController = wordController;
    }

    public void run() {
        //Starts the game by generating the words and assigning them to WordThread objects.
        wordController.startWords();

        //Loops until the game is ended.
        while (!wordController.ended()) {
            //Repaints if the model has changed.
            if (wordController.isChanged()) {
                repaint();
                wordController.setUnchanged();
            }
            //Makes the thread sleep for 2ms to prevent a large number of unnecessary calls.
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Repaints the panel once.
     */
    public void repaintOnce() {
        repaint();
    }
}
