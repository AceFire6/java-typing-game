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
        g.clearRect(0,0,width,height);
        g.setColor(Color.red);
        g.fillRect(0,maxY-10,width,height);

        g.setColor(Color.black);
        g.setFont(new Font("Helvetica", Font.PLAIN, 26));
        //draw the words
        //animation must be added
        for (int i=0;i<noWords;i++){
            // Some words stuck out.
            g.drawString(words[i].getWord(),words[i].getX(),words[i].getY() - 5);
//            g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words
        }

    }

    public WordPanel(WordRecord[] words, int maxY, WordController wordController) {
        this.words=words; //will this work?
        noWords = words.length;
        this.maxY=maxY;
        this.wordController = wordController;
    }

    public void run() {
        //add in code to animate this
        wordController.startWords();
        while (!wordController.ended()) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void repaintOnce() {
        repaint();
    }
}
