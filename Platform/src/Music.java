import java.io.*;
import javax.sound.sampled.*;

public class Music implements Runnable {
    Thread t;
    File audioFile;
    AudioInputStream audioStream;
    Clip audioClip;
    String fn;

    public Music(String fileName, boolean loops) {
        fn = fileName;
        audioFile = new File(fileName);
        try {
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioStream);

            if (loops) {
                audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        start3();
    }

    public void start3() {
        t = new Thread(this, fn);
        start2();
        t.start();
    }

    public void start() {
        t = new Thread(this, fn);
        t.start();
    }

    public void start2() {
        audioFile = new File(fn);
        try {
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioStream);
            audioClip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        audioClip.start();
    }
}
