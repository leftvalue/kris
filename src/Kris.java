import commands.Default;
import commands.Tencent;
import io.airlift.command.Cli;
import io.airlift.command.Help;

/**
 * @author linxi
 * www.leftvalue.top
 * PACKAGE_NAME
 * Date 2018/7/27 4:29 PM
 */
public class Kris {
    public static void main(String[] args) throws Exception {
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("kris")
                .withDescription("kristendi's tools that's change the world")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Default.ShortenURL.class, Default.Test.class);
        builder.withGroup("tencent")
                .withDescription("some tools for daily use in tencent")
                .withDefaultCommand(Tencent.Mail.class)
                .withCommands(Tencent.Mail.class);
        Cli<Runnable> krisParser = builder.build();
        try {
            krisParser.parse(args).run();
        } catch (Exception e) {
            System.out.println("Sorry for " + e.getMessage() + ". Have a nice day :)");
        }
    }
}
