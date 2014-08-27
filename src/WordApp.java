import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
//model is separate from the view.

public class WordApp {
    //shared variables
    static int noWords = 4;
    static int totalWords;

    static int frameX = 1000;
    static int frameY = 600;
    static int yLimit = 480;

    static WordDictionary dict = new WordDictionary();
    //use default dictionary, to read from file eventually

    static WordRecord[] words;
    static volatile boolean done;  //must be volatile
    static Score score = new Score();

    static WordPanel w;
    /**
     * An array of the JLabels that need to be given
     * new text when the scores are updated.
     * They are passed to the wordController so it can
     * handle their retetxing.
     */
    static JLabel[] labels;
    /**
     * Provides a way for the threads to interact with different objects
     * safely.
     */
    static WordController wordController;


    public static void setupGUI(int frameX, int frameY, int yLimit) {
        // Frame init and dimensions
        JFrame frame = new JFrame("WordGame");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(frameX, frameY);

        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setSize(frameX, frameY);


        w = new WordPanel(words, yLimit, wordController);
        wordController.addPanel(w);
        w.setSize(frameX, yLimit + 100);
        g.add(w);


        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));
        JLabel caught = new JLabel("Caught: " + score.getCaught() + "    ");
        JLabel missed = new JLabel("Missed:" + score.getMissed() + "    ");
        JLabel scr = new JLabel("Score:" + score.getScore() + "    ");
        JLabel incorrect = new JLabel("Incorrect Attempts:" + score.getIncorrectWords() + "    ");
        labels = new JLabel[] {caught, missed, scr, incorrect};
        wordController.addLabels(labels);
        txt.add(caught);
        txt.add(missed);
        txt.add(scr);
        txt.add(incorrect);

        //Gets the text entry from the textfield and compares it to the words.

        final JTextField textEntry = new JTextField("", 20);
        textEntry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!wordController.isPaused() && wordController.isRunning()) {
                    String text = textEntry.getText();
                    if (!wordController.checkWord(text)) {
                        score.inncorrectWord();
                        wordController.setChanged();
                    }
                }
                textEntry.setText("");
                textEntry.requestFocus();
            }
        });

        txt.add(textEntry);
        txt.setMaximumSize(txt.getPreferredSize());
        g.add(txt);

        JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
        JButton startB = new JButton("Start");

        // add the listener to the jbutton to handle the "pressed" event
        startB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Starts the panel's run() in a separate thread.
                if (!wordController.isRunning()) {
                    new Thread(w).start();
                } else if (wordController.isPaused()) {
                    wordController.setPaused();
                }
                textEntry.requestFocus();  //return focus to the text entry field
            }
        });

        JButton pauseB = new JButton("Pause");

        // add the listener to the jbutton to handle the "pressed" event
        pauseB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // If the game is running. Set its state to paused.
                if (wordController.isRunning()) {
                    wordController.setPaused();
                }
                textEntry.setText("");
                textEntry.requestFocus();  //return focus to the text entry field
            }
        });

        JButton resetB = new JButton("Reset");

        // add the listener to the jbutton to handle the "pressed" event
        resetB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Resets the ccurrent game board.
                if (wordController.isRunning()) {
                    wordController.endGame();
                }
            }
        });

        b.add(startB);
        b.add(pauseB);
        b.add(resetB);

        g.add(b);

        frame.setLocationRelativeTo(null);  // Center window on screen.
        frame.add(g); //add contents to window
        frame.setContentPane(g);
        //frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

    }


    public static String[] getDictFromFile(String filename) {
        String[] dictStr = null;
        try {
            Scanner dictReader = new Scanner(new FileInputStream(filename));
            int dictLength = dictReader.nextInt();
            //System.out.println("read '" + dictLength+"'");

            dictStr = new String[dictLength];
            for (int i = 0; i < dictLength; i++) {
                dictStr[i] = dictReader.next();
                //System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
            }
            dictReader.close();
        } catch (IOException e) {
            System.err.println(
                    "Problem reading file " + filename + " default dictionary will be used");
        }
        return dictStr;

    }

    public static void main(String[] args) {
        done = true;
        //deal with command line arguments
        totalWords = Integer.parseInt(args[0]);  //total words to fall
        noWords = Integer.parseInt(args[1]); // total words falling at any point
        assert (totalWords >= noWords); // this could be done more neatly
        String[] tmpDict = getDictFromFile(args[2]); //file of words
        if (tmpDict != null) {
            dict = new WordDictionary(tmpDict);
        }

        WordRecord.dict = dict; //set the class dictionary for the words.

        words = new WordRecord[noWords];  //shared array of current words

        //[snip]

        int x_inc = frameX / noWords;
        //initialize shared array of current words

        for (int i = 0; i < noWords; i++) {
            words[i] = new WordRecord(dict.getNewWord(), i * x_inc, yLimit);
        }
        wordController = new WordController(totalWords, score, words);

        setupGUI(frameX, frameY, yLimit);
        //Start WordPanel thread - for redrawing animation
    }
}
