package extensions;

import basetool.Port;
import com.alibaba.fastjson.JSON;
import spark.Spark;

import java.io.File;
import java.net.URLDecoder;
import java.util.LinkedList;

import static spark.Spark.*;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/7 5:16 PM
 */
public class SparkHttpServer {

    private static final String NotFoundStr = "{\"message\":\"你寻找的页面去了火星,追随着它的理想\"}";
    private int port = 80;
    private String content;
    private String filePath;

    public SparkHttpServer setPort(int port) {
        this.port = port;
        return this;
    }

    public SparkHttpServer setContent(String content) {
        this.content = content;
        return this;
    }

    public SparkHttpServer setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    private static String getFilesInfo(String path) {
        try {
            File dir = new File(path);
            File[] child_files = dir.listFiles(file -> {
                return true;
            });
            LinkedList<String> fss = new LinkedList<>();
            fss.add(".");
            fss.add("..");
            for (File child : child_files) {
                fss.add(child.getName());
            }
            return JSON.toJSONString(fss);
        } catch (Exception e) {
            return NotFoundStr;
        }
    }

    public void start() {
        try {
            if (Port.isLocalPortUsing(port)) {
                System.out.println("localhost:" + port + " is using\nchange to localhost:" + (port + 1));
                port += 1;
                start();
                return;
            }
            port(port);
            if (filePath != null) {
                File webroot = new File(filePath);
                if (webroot.exists() && webroot.isDirectory()) {
                    this.filePath = webroot.getAbsolutePath();
                    staticFiles.externalLocation(webroot.getAbsolutePath());
                    System.out.println("File Server listen on http://127.0.0.1:" + port);
                    notFound((req, res) -> {
                        String uri = URLDecoder.decode(req.uri());
                        /**
                         * 如果不存在的路径就返回文件夹的详细信息
                         */
                        String path = this.filePath + uri;
                        res.type("application/json");
                        return getFilesInfo(path);
                    });
                    Spark.init();
                    return;
                }
            } else {
                notFound((req, res) -> {
                    res.type("application/json");
                    return NotFoundStr;
                });
                if (content != null) {
                    /**
                     * listen to all route
                     */
                    get("/"
                            , (req, res) -> content);
                    System.out.println("Simple Content server listen on http://127.0.0.1:" + port);
                } else {
                    System.out.println("Please set at least content or filePath before start server");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("server start failed");
        }
    }
}

