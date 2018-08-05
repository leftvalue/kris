package extensions;

import basetool.OuterFileReader;
import org.mozilla.intl.chardet.nsDetector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/3 2:54 PM
 */
public class Chardetect {
    private static final String BOM = new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
    private String from_path;
    private String to_path;

    public Chardetect(String from_path, String to_path) {
        File f = new File(from_path);
        if (!f.exists()) {
            System.out.println("指定文件不存在");
        }
        this.from_path = from_path;
        this.to_path = to_path;
    }

    public Chardetect(String from_path) {
        File f = new File(from_path);
        if (!f.exists()) {
            System.out.println("指定文件不存在");
        }
        this.from_path = from_path;
    }

    public boolean addBom() {
        try {
            String ori_content = new OuterFileReader(from_path).read();
            BufferedWriter out = Files.newBufferedWriter(Paths.get(to_path));
            out.write(BOM);
            out.write(ori_content);
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean found = false;
    private String encoding = null;

    public String guessFileEncoding() {
        try {
            File file = new File(this.from_path);
            return guessFileEncoding(file, new nsDetector());
        } catch (Exception e) {
            return "cannot guess the true file encoding of your file :(";
        }
    }

    /**
     * 获取文件的编码
     *
     * @param file
     * @param det
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String guessFileEncoding(File file, nsDetector det) throws FileNotFoundException, IOException {
        // Set an observer...
        // The Notify() will be called when a matching charset is found.
        det.Init(charset -> {
            encoding = charset;
            found = true;
        });
        BufferedInputStream imp = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = false;
        while ((len = imp.read(buf, 0, buf.length)) != -1) {
            /**
             * Check if the stream is only ascii.
             */
            isAscii = det.isAscii(buf, len);
            if (isAscii) {
                break;
            }
            /**
             * DoIt if non-ascii and not done yet.
             */
            done = det.DoIt(buf, len, false);
            if (done) {
                break;
            }
        }
        imp.close();
        det.DataEnd();

        if (isAscii) {
            encoding = "ASCII";
            found = true;
        }

        if (!found) {
            String[] prob = det.getProbableCharsets();
            //这里将可能的字符集组合起来返回
            for (int i = 0; i < prob.length; i++) {
                if (i == 0) {
                    encoding = prob[i];
                } else {
                    encoding += "," + prob[i];
                }
            }
            if (prob.length > 0) {
                /**
                 * 在没有发现情况下,也可以只取第一个可能的编码,这里返回的是一个可能的序列
                 */
                return encoding;
            } else {
                return null;
            }
        }
        return encoding;
    }

}
