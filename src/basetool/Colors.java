package basetool;

import org.fusesource.jansi.Ansi;

import java.util.LinkedList;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/5 2:22 PM
 * get string that print with color
 */
public class Colors {
    static LinkedList<Ansi.Color> colors = new LinkedList<>();
    public static int count = 0;

    static {
        colors.add(RED);
        colors.add(GREEN);
        colors.add(YELLOW);
        colors.add(BLUE);
        colors.add(MAGENTA);
        colors.add(CYAN);
        colors.add(WHITE);
    }

    private static boolean ifColorful = true;

    /**
     * 这里有一个坑爹的地方,先前忽略了没有看 api 导致出错
     * .eraseScreen()方法用来清楚当前屏幕,不要在连续调用中调用,否则一页一行字太感人
     *
     * @param str
     */
    public static void println(Object str) {
        if (ifColorful) {
            System.out.println(ansi().fg(colors.get(((count++) % colors.size()))).a(str.toString()).reset());
        } else {
            System.out.println(str);
        }
    }

    public static void print(Object str) {
        if (ifColorful) {
            System.out.print(ansi().fg(colors.get(((count) % colors.size()))).a(str.toString()).reset());
        } else {
            System.out.print(str);
        }
    }

    public static String getCS(Ansi.Color color, String str) {
        return ansi().fg(color).a(str).reset().toString();
    }

}
