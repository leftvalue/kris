package extensions;

import java.io.File;
import java.io.IOException;
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
                f.mkdirs();
            }
            watch(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void watch(String filepath) throws IOException,
            InterruptedException {
        WatchService service = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(filepath).toAbsolutePath();
        path.register(
                service, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        while (true) {
            WatchKey key = service.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                System.out.println(event.kind().toString());
                if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                    Path createdPath = (Path) event.context();
                    createdPath = path.resolve(createdPath);
                    long size = Files.size(createdPath);
                    System.out.println("创建文件：" + createdPath + "==>" + size);
                } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    Path createdPath = (Path) event.context();
                    createdPath = path.resolve(createdPath);
                    long size = Files.size(createdPath);
                    System.out.println("修改文件：" + createdPath + "==>" + size);
                } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                    Path createdPath = (Path) event.context();
                    createdPath = path.resolve(createdPath);
                    System.out.println("删除 文件：" + createdPath);
                }
            }
            key.reset();
        }
    }

    public static void main(String[] args) {
        new Ding().fileMonitor("/Users/linxi/Downloads/1");
    }
}
