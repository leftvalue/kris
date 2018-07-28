package commands;

import basetool.SystemClipboardTools;
import extensions.MailHtmlCreater;
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;

/**
 * @author linxi
 * www.leftvalue.top
 * commands
 * Date 2018/7/28 10:59 PM
 */
public class Tencent {
    @Command(name = "mail", description = "Generate weekly mail template of tencent (auto copy to clipboard)")
    public static class Mail implements Runnable {
        @Arguments(description = "rows of works")
        public int row_count = 2;
        @Option(name = "-u", description = "the name of you (reporter,email sender)")
        public String author;

        @Option(name = "-t", description = "title of the email")
        public String title = "工作周报";

        @Override
        public void run() {
            if (row_count < 1) {
                row_count = 1;
            }
            String template = new MailHtmlCreater().getSimpleTemplate(author, title, row_count);
            SystemClipboardTools.write(template);
            System.out.println("Success generate and copy to your clipboard !");
        }
    }
}
