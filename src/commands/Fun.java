package commands;

import basetool.SystemClipboardTools;
import extensions.SoundTypeWriter;
import io.airlift.command.Command;
import io.airlift.command.Option;

/**
 * @author linxi
 * www.leftvalue.top
 * commands
 * Date 2018/8/5 3:26 PM
 */
public class Fun {
    @Command(name = "qinqin", description = "a magic colorful typewriter, get input from system clipboard, print with G80 sounds")
    public static class TypeWriter implements Runnable {
        @Option(name = {"-s", "--speed"}, description = "the speed of the type writer:low normal high")
        public String speed = "normal";

        @Override
        public void run() {
            String str = SystemClipboardTools.get();
            if (str.trim().isEmpty()) {
                System.out.println("Sorry but your system clipboard is empty,please copy some words");
                return;
            }
            speed = speed.toUpperCase();
            if (speed.startsWith("L")) {
                new SoundTypeWriter(str, 1.25).print();
            } else if (speed.startsWith("H")) {
                new SoundTypeWriter(str, 0.7).print();
            } else {
                new SoundTypeWriter(str).print();
            }
        }
    }


}
