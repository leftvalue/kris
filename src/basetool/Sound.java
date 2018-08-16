package basetool;


/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/5 3:00 PM
 */


import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {
    public static final String basePath = "/sounds/Cherry_G80_3494/G80-3494";

    private void ring(String name) {
        try {
            // 1.wav 文件放在java project 下面
            AudioStream as = new AudioStream(this.getClass().getResourceAsStream(basePath + name + ".wav"));
            AudioPlayer.player.start(as);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String name) {
        try {
            // 1.wav 文件放在java project 下面
            AudioStream as = new AudioStream(this.getClass().getResourceAsStream("/" + name));
            AudioPlayer.player.start(as);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iphone() {
        play("iphone.wav");
    }

    public void baidu() {
        play("baidu.wav");
    }

    public void normal() {
        ring("");
    }

    public void backspace() {
        ring("_backspace");
    }

    public void enter() {
        ring("_enter");
    }

    public void space() {
        ring("_space");
    }

}