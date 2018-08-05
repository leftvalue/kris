package extensions;

import basetool.Colors;
import commands.Default;
import commands.Tencent;
import io.airlift.command.Cli;
import io.airlift.command.Help;
import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/5 1:44 PM
 */
public class SuperTerminalMode extends Thread {
    private static String[] Candidates = new String[]{"help", "ip", "shorturl", "encode", "tencent", "mail", "servers"};
    private static final String prefix = Colors.getCS(Ansi.Color.GREEN, "~> ");

    @Override
    public void run() {
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new StringsCompleter(Candidates))
                    .build();
            String line = "";
            while (true) {
                if (!(line = lineReader.readLine(prefix)).equals("")) {
                    String[] args = line.split(" ");
                    main(args);
                } else {
                    System.out.println(prefix);
                }
            }
        } catch (UserInterruptException e) {
            System.out.println(Colors.getCS(Ansi.Color.YELLOW, "\nGoodby bye and have a nice day ~ :)"));
        } catch (org.jline.reader.EndOfFileException e) {
            System.out.println(Colors.getCS(Ansi.Color.YELLOW, "\nGoodby bye and have a nice day ~ :)"));
        } catch (IOException e) {
            System.out.println(Colors.getCS(Ansi.Color.RED, "Cannot run on terminal mode, please run on default (command) mode "));
        }
    }

    public void main(String[] args) {
        /**
         * remember not all command is suitable to run on super terminal mode,
         * such as httpproxy
         * which do not end until u shut it down by ctrl-c or just kill
         * Also ,you cannot start another terminal in the super terminal
         */
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("kris")
                .withDescription("kristendi's tools that's change the world")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Default.ShortenURL.class,
                        Default.Encode.class, Default.Test.class
                        , Default.Ip.class);
        builder.withGroup("tencent")
                .withDescription("some tools for daily use in tencent")
                .withDefaultCommand(Tencent.Mail.class)
                .withCommands(Tencent.Mail.class, Tencent.ServerParser.class);
        Cli<Runnable> krisParser = builder.build();
        try {
            krisParser.parse(args).run();
        } catch (Exception e) {
            krisParser.parse("help").run();
        }
    }
}
