package extensions;

import java.io.File;
import java.nio.file.*;
import java.util.List;

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
    public void fileMonitor(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                System.out.println("target '" + path + "' doesn't exist");
                f.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Path myDir = Paths.get(path);
        try {
            WatchService watcher = myDir.getFileSystem().newWatchService();
            myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watckKey = watcher.take();
            List<WatchEvent<?>> events = watckKey.pollEvents();
            for (WatchEvent event : events) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    System.out.println("Created: " + event.context().toString());
                }
                if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Delete: " + event.context().toString());
                }
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    System.out.println("Modify: " + event.context().toString());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }
    public static void main(String[] args){
        new Ding().fileMonitor("/Users/linxi/Downloads/temp");
    }
}
