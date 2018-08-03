package test;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * @author linxi
 * www.leftvalue.top
 * test
 * Date 2018/7/30 4:32 PM
 */
public class JlineTest {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.terminal();
        terminal.echo();
    }
}
