package extensions;

import basetool.Colors;
import basetool.Sound;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/5 3:39 PM
 */
public class SoundTypeWriter {
    private String str;

    public SoundTypeWriter(String str) {
        this.str = str;
    }

    public SoundTypeWriter(String str, double speed) {
        this.str = str;
        this.speed = speed;
    }

    private double speed = 1;

    public void print() {
        int char_sleep_time = new Double(200 * speed).intValue();
        int space_more_sleep_time = new Double(50 * speed).intValue();
        String[] lines = str.split("\n");
        Sound sound = new Sound();
        for (String line : lines) {
            String[] words = line.split("\\s");
            for (String word : words) {
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == ' ' || word.charAt(i) == '\t') {
                        System.out.println(" ");
                    } else {
                        sound.normal();
                        Colors.print(word.charAt(i));
                        sleep(char_sleep_time);
                    }
                }
                sleep(space_more_sleep_time);
                sound.space();
                System.out.print(" ");
            }
            sound.enter();
            System.out.println("");
            Colors.count++;
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }
}
