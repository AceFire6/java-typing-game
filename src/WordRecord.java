public class WordRecord {
    private String text;
    private int x;
    private int y;
    private int maxY;
    private boolean missed;
    private boolean reset;

    private int fallingSpeed;

    public static WordDictionary dict;


    public WordRecord() {
        text = "";
        x = 0;
        y = 0;
        maxY = 300;
        missed = false;
        reset = false;
        setFallingSpeed();
    }

    public WordRecord(String text) {
        this();
        this.text = text;
    }

    public WordRecord(String text, int x, int maxY) {
        this(text);
        this.x = x;
        this.maxY = maxY;
    }

    // all getters and setters must be synchronized
    public synchronized void setY(int y) {
        if (y > maxY) {
            y = maxY;
            missed = true;
        }
        this.y = y;
    }

    public synchronized void setX(int x) {
        this.x = x;
    }

    public synchronized void setWord(String text) {
        this.text = text;
    }

    public synchronized String getWord() {
        return text;
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized int getSpeed() {
        return fallingSpeed;
    }

    public synchronized void setPos(int x, int y) {
        setY(y);
        setX(x);
    }

    public synchronized void resetPos() {
        setY(0);
    }

    public synchronized void resetWord() {
        resetPos();
        text = dict.getNewWord();
        missed = false;
        reset = true;
        setFallingSpeed();
        //System.out.println(getWord() + " falling speed = " + getSpeed());

    }

    public boolean isReset() {
        return reset;
    }

    public synchronized boolean matchWord(String typedText) {
        //System.out.println("Matching against: "+text);
        if (typedText.equals(this.text)) {
            resetWord();
            return true;
        } else {
            return false;
        }
    }

    public void setFallingSpeed() {
        int maxWait = 2000;
        int minWait = 200;
//        System.out.println("BEFORE: " + fallingSpeed);
        fallingSpeed = (int) (Math.random() * (maxWait - minWait) + minWait);
//        System.out.println("AFTER: " + fallingSpeed);
    }

    public synchronized void drop(int inc) {
        setY(y + inc);
    }

    public synchronized boolean missed() {
        return missed;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        WordRecord wrdObj = ((WordRecord) obj);

        return (this.getWord().equals(wrdObj.getWord()) && (this.getY() == wrdObj.getY()));
    }
}
