package commands;

import basetool.SystemClipboardTools;
import extensions.EmlParser;
import extensions.MailHtmlCreater;
import io.airlift.command.Arguments;
import io.airlift.command.Command;
import io.airlift.command.Option;

import java.util.LinkedList;

/**
 * @author linxi
 * www.leftvalue.top
 * commands
 * Date 2018/7/28 10:59 PM
 */
public class Tencent {
    @Command(name = "mail", description = "Generate weekly mail template of tencent")
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
//            SystemClipboardTools.write(template);
//            System.out.println("Success generate and copy to your clipboard !");
            System.out.println(template);
        }
    }

    @Command(name = "servers", description = "get server ip and password from emls dir(export from foxmail)")
    public static class ServerParser implements Runnable {
        @Arguments(description = "dir of emls")
        public String dir;

        @Option(name = "-a", description = "output alias commands")
        public boolean isAliasCommand = false;

        @Option(name = "-prefix", description = "prefix of alias commands")
        public String prefix = "z";

        /**
         * because there is no origin command starts with z
         */
        @Override
        public void run() {
            try {
                LinkedList<EmlParser.Server> servers = new EmlParser().getServers(dir);
                StringBuilder sb = new StringBuilder();
                servers.forEach(s -> {
                    if (ServerParser.this.isAliasCommand) {
                        sb.append("alias " + prefix + s.getLast() + "='echo \"" + s.getPassword().replace("$", "\\$") + "\";ssh webroot@" + s.getIp() + "'\n");
                    } else {
                        sb.append(s + "\n");
                    }
                });
                String output = sb.toString();
                System.out.println(output);
                SystemClipboardTools.write(output);
            } catch (Exception e) {
                System.out.println("Sorry,but get no server info");
            }
        }
    }
}
