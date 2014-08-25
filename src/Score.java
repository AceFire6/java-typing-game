import java.util.concurrent.atomic.AtomicInteger;

public class Score {
    private AtomicInteger missedWords;
    private AtomicInteger caughtWords;
    private AtomicInteger gameScore;
    private AtomicInteger incorrectWords;

    public Score() {
        //Used AtomicIntegers to reduces the need for blocking with
        //Synchronization
        missedWords = new AtomicInteger(0);
        caughtWords = new AtomicInteger(0);
        gameScore = new AtomicInteger(0);
        incorrectWords = new AtomicInteger(0);
    }

    // all getters and setters must be synchronized

    public int getMissed() {
        return missedWords.get();
    }

    public int getCaught() {
        return caughtWords.get();
    }

    public synchronized int getTotal() {
        return (missedWords.addAndGet(caughtWords.get()));
    }

    public int getScore() {
        return gameScore.get();
    }

    public void missedWord() {
        missedWords.incrementAndGet();
    }

    public synchronized void caughtWord(int length) {
        caughtWords.getAndIncrement();
        gameScore.addAndGet(length);
    }

    public synchronized void resetScore() {
        missedWords.set(0);
        caughtWords.set(0);
        gameScore.set(0);
        incorrectWords.set(0);
    }

    public int getIncorrectWords() {
        return incorrectWords.get();
    }

    public void inncorrectWord() {
        incorrectWords.incrementAndGet();
    }
}
