package commands;

import extensions.AShortUrl;
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;
import io.airlift.command.OptionType;

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
}
