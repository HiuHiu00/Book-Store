
package EmailSender;

public class CountdownInfo {
    private int duration;
    private long startTime;

    public CountdownInfo(int duration, long startTime) {
        this.duration = duration;
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isValid() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000; 
        return elapsedTime < duration;
    }
}
