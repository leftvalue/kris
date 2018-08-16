package extensions.ding;

import basetool.*;

import java.io.File;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/16 9:45 AM
 * when u calc a large num of data
 * u do not want to focus on the screen
 * but
 * how could u know when it run over?
 * So i create a module that can listen to a port/local file
 * once you send a request to the port or write the local file
 */
public class Ding {
    private AbstractNotice notice = new DingdingNotice();

    public void fileMonitor(String path) {
        try {
            File f = new File(path).getAbsoluteFile();
            if (!f.exists()) {
                System.out.println("target '" + path + "' doesn't exist,create now");
                OuterDir.createFile(f.getAbsolutePath());
            }
            FileWatcher watcher = new FileWatcher(path, notice);
            watcher.setDaemon(false);
            watcher.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void portMonitor(int port) {
        try {
            SimpleHttpPortListener server = new SimpleHttpPortListener();
            server.setNotice(new DingdingNotice());
            server.setPort(port);
            server.setDaemon(false);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Ding d = new Ding();
        d.portMonitor(1234);
        d.fileMonitor("/Users/linxi/Downloads/1/1");
    }
}

