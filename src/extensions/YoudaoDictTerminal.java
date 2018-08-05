package extensions;

import basetool.Colors;
import org.fusesource.jansi.Ansi;
import org.jline.reader.*;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/6 12:29 AM
 */
public class YoudaoDictTerminal {
    private static final String prefix = Colors.getCS(Ansi.Color.GREEN, " -> ");

    private static String[] Candidates = new String[]{"help", "ip", "shorturl", "encode", "tencent", "mail", "servers"};

    public static void start() {
        try {
            Terminal terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new StringsCompleter(Candidates))
                    .completer(new Completer() {
                        @Override
                        public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
                            String keyword = parsedLine.word();
                            LinkedList<String> candidates = YoudaoDictComplete.getCandidates(keyword);
                            for (String candidate : candidates) {
                                list.add(new Candidate(candidate));
                            }
                        }
                    })
                    .build();
            String line = "";
            while (true) {
                if (!(line = lineReader.readLine(prefix)).equals("")) {
                    YoudaoDict.searchEng(line);
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
}
