package basetool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/3 3:21 PM
 */
public class OuterDir {
    /**
     * 递归地得到子目录下的所有指定拓展名的文件
     *
     * @param fromdir
     * @param extensions
     * @return
     * @throws IOException
     */
    public static Collection<File> listFiles(String fromdir, String... extensions) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(fromdir), extensions, true);
        return files;
    }

    public static Collection<File> listFiles(String fromdir) throws IOException {
        Collection<File> files = FileUtils.listFilesAndDirs(new File(fromdir), new FileFileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }
        }, null);
        return files;
    }

    /**
     * 创建多级目录文件
     *
     * @param path 文件路径
     * @throws IOException
     */
    public static void createFile(String path) {
        try {
            if (StringUtils.isNotEmpty(path)) {
                File file = new File(path);
                if (!file.getParentFile().exists() || (file.getParentFile().isFile())) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
