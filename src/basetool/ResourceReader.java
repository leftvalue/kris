package basetool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/7/28 11:43 PM
 */
public class ResourceReader {
    private String path;

    private static enum PATTERN {IDE, JAR, NOTFOUND}

    private PATTERN pattern;

    public ResourceReader(String path) {
        this.path = path;
        if (new File(path).exists()) {
            this.pattern = PATTERN.IDE;
        } else {
            try {
                InputStream is = this.getClass().getResourceAsStream(this.path);
                is.close();
                this.pattern = PATTERN.JAR;
            } catch (Exception e) {
                e.printStackTrace();
                this.pattern = PATTERN.NOTFOUND;
            }
        }
    }

    public String read() {
        /**
         * 一般用于读取项目本身的一些配置文件,而非程序运行时需要外部提供并读取的文件
         */
        try {
            if (this.pattern == PATTERN.NOTFOUND) {
                return "";
            } else if (this.pattern == PATTERN.IDE) {
                /**
                 * 路径前没有 / 放在项目根目录,在这里访问到
                 */
                return new String(Files.readAllBytes(Paths.get(this.path)));
            } else {
                /***
                 * 路径前加 /
                 *
                 * 放在 src 下
                 * 设置为 resource dir
                 * 不管是 jar 还是 ide 都能从这里访问到
                 */
                /**
                 * JAR
                 */
                InputStream is = this.getClass().getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String s = "";
                StringBuffer sb = new StringBuffer();
                while ((s = br.readLine()) != null) {
                    sb.append(s + "\n");
                }
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
