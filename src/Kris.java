import commands.Default;
import commands.Fun;
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
    public static void main(String[] args) {
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("kris")
                .withDescription("kristendi's tools that's change the world")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Default.ShortenURL.class,
                        Default.Encode.class, Default.Test.class
                        , Default.Ip.class, Default.HttpProxy.class, Default.TerminalMode.class);
        builder.withGroup("tencent")
                .withDescription("some tools for daily use in tencent")
                .withDefaultCommand(Tencent.Mail.class)
                .withCommands(Tencent.Mail.class, Tencent.ServerParser.class);
        builder.withGroup("fun")
                .withDescription("some thing just for fun :)")
                .withDefaultCommand(Fun.TypeWriter.class)
                .withCommands(Fun.TypeWriter.class);
        Cli<Runnable> krisParser = builder.build();
        try {
            krisParser.parse(args).run();
        } catch (Exception e) {
            System.out.println("Invalid Command :(");
            krisParser.parse("helo").run();
        }
    }
}
