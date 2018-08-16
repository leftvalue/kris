package extensions.ding;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/16 3:35 PM
 */
public class FileWatcher extends Thread {
    @Override
    public void run() {
        try {
            this.watchSingleFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Executor threads_poll = Executors.newCachedThreadPool();
    private String path;
    private AbstractNotice notice;

    public FileWatcher(String path, AbstractNotice notice) {
        this.path = path;
        this.notice = notice;
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    public void watchSingleFile() throws IOException,
            InterruptedException {
        File f = new File(this.path).getAbsoluteFile();
        final String fix_filePath = f.getAbsolutePath();
        String parentPath = f.getParent();
        WatchService service = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(parentPath).toAbsolutePath();
        path.register(
                service, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        System.out.println("watch on '" + fix_filePath + "'");
        while (true) {
            WatchKey key = service.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                Path createdPath = (Path) event.context();
                createdPath = path.resolve(createdPath);
                String dynamic_file_path = createdPath.toString();
                if (dynamic_file_path.equals(fix_filePath)) {
                    if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                        System.out.println("create");
                    } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                        this.notice.setMessage("PONG");
                        threads_poll.execute(notice);
                    } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        System.out.println("target '" + fix_filePath + "' has been remove, recreate it");
                        new File(fix_filePath).createNewFile();
                    }
                }
            }
            key.reset();
        }
    }
}
