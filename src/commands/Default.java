package commands;

import extensions.*;
import extensions.ding.Ding;
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;
import io.airlift.command.OptionType;
import me.tongfei.progressbar.ProgressBar;

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
        @Option(type = OptionType.COMMAND, name = {"--cache"}, description = "if you want to cache all response file,give the argv")
        public String cache_path = "";

        @Override
        public void run() {
            Proxy p = new extensions.Proxy(port, keyword);
            if (!cache_path.equals("")) {
                p.cache(cache_path);
            }
            p.listen();
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

    @Command(name = "httpserver", description = "start a simple http server")
    public static class KrisServer implements Runnable {
        @Option(name = {"-p", "--port"}, description = "the port that server listen")
        public int port = 80;
        @Option(name = "--path", description = "the path of the file server")
        public String filepath;
        @Option(name = {"-c", "--content"}, description = "the response body that server will return")
        public String content;

        @Override
        public void run() {
            SparkHttpServer server = new SparkHttpServer();
            server.setPort(port);
            if (content != null) {
                server.setContent(content);
            }
            if (filepath != null) {
                server.setFilePath(filepath);
            }
            server.start();
        }
    }

    @Command(name = "ding", description = "listen on a port and watch a file,once you send a request to the port or modify the file ,ring 3 times to alert you")
    public static class Dingding implements Runnable {
        @Option(name = {"-p", "--port"}, description = "the port that be listen")
        public int port = 1234;
        @Option(name = "--path", description = "the path to watch")
        public String filepath = "ding";

        @Override
        public void run() {
            Ding d = new Ding();
            d.portMonitor(port);
            d.fileMonitor(filepath);
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
