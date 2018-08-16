package basetool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/3 3:00 PM
 * 并不是用于读取项目的配置文件
 * 而是一些运行时用户指定的文件
 */
public class OuterFileReader {
    private Path path;

    public OuterFileReader(String path) {
        this.path = Paths.get(path);
    }

    public String read() throws IOException {
        return new String(Files.readAllBytes(this.path));
    }
}
