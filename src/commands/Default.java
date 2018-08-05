package commands;

import extensions.AShortUrl;
import extensions.SuperTerminalMode;
import extensions.Chardetect;
import extensions.YoudaoDictTerminal;
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import me.tongfei.progressbar.ProgressBar;
import test.JlineTest;

import java.net.InetAddress;

/**
 * @author linxi
 * www.leftvalue.top
 * commands
 * Date 2018/7/27 4:49 PM
 */
public class Default {
    public static class KrisCommand implements Runnable {
        @Option(type = OptionType.GLOBAL, name = "-v", description = "Verbose mode")
        public boolean verbose;

        @Override
        public void run() {
            System.out.println(getClass().getSimpleName());
        }
    }

    @Command(name = "shorturl", description = "Use dwz.cn service to shorten your long long url")
    public static class ShortenURL implements Runnable {
        @Arguments(description = "URL to be shorten")
        public String url;

        @Override
        public void run() {
            System.out.println(new AShortUrl().execute(url));
        }
    }

    @Command(name = "encode", description = "About file encoding . . .(such as guess encoding/add BOM)")
    public static class Encode implements Runnable {
        @Option(type = OptionType.COMMAND, name = {"-f", "--from"}, description = "source file path")
        public String frompath;
        @Option(type = OptionType.COMMAND, name = {"-t", "--to"}, description = "target path after handle")
        public String topath;
        @Option(type = OptionType.COMMAND, name = {"-b", "--bom"}, description = "add BOM head to file (because excel believe a file encode with utf-8 when detect it with a BOM head)")
        public boolean addBom;
        @Option(type = OptionType.COMMAND, name = {"-g", "--guess"}, description = "guess the encode of one file")
        public boolean guess;

        @Override
        public void run() {
            if (addBom) {
                new Chardetect(frompath, topath).addBom();
            } else if (guess) {
                System.out.println(new Chardetect(frompath).guessFileEncoding());
            }
        }
    }

    @Command(name = "ip", description = "get LocalHost HostName / CanonicalHostName")
    public static class Ip implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress address = InetAddress.getLocalHost();
                System.out.println(address.getCanonicalHostName());
                System.out.println(address.getHostName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Command(name = "proxy", description = "A http proxy that allow u to tracert source of some information")
    public static class HttpProxy implements Runnable {
        @Option(type = OptionType.COMMAND, name = {"-p", "--port"}, description = "the port that the proxy listen(default 8888)")
        public int port = 8888;
        @Option(type = OptionType.COMMAND, name = {"-k", "--keyword"}, description = "regex pattern of keyword to match your target response body(default empty,run on transparent mode without analyse)")
        public String keyword = "";

        @Override
        public void run() {
            new extensions.Proxy(port, keyword).listen();
        }
    }

    @Command(name = "terminal", description = "Run on super terminal mode")
    public static class TerminalMode implements Runnable {
        @Override
        public void run() {
            new SuperTerminalMode().start();
        }
    }

    @Command(name = "dict", description = "Youdao dictionary terminal with auto completion when <TAB> key down")
    public static class Youdao implements Runnable {
        @Override
        public void run() {
            YoudaoDictTerminal.start();
        }
    }

    @Command(name = "ok", description = "")
    public static class Test implements Runnable {

        @Override
        public void run() {
            try (ProgressBar pb = new ProgressBar("test", 100)) {
                for (int i = 0; i < 100; i++) {
                    pb.step(); // step by 1
                    pb.setExtraMessage("Reading...");
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }
}
