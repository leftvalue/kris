package extensions;

import basetool.OuterFileReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/7/31 4:18 PM
 */
public class EmlParser {
    public static class Server {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private String ip;
        private String password;
        private LocalDateTime ddl;

        public Server(String ip, String password, String ddl) {
            this.ip = ip;
            this.password = password;
            this.ddl = LocalDateTime.parse(ddl, formatter);
        }

        public static DateTimeFormatter getFormatter() {
            return formatter;
        }

        public String getIp() {
            return ip;
        }

        public String getPassword() {
            return password;
        }

        public LocalDateTime getDdl() {
            return ddl;
        }

        public boolean isAfter(Server s) {
            return this.ddl.isAfter(s.ddl);
        }

        @Override
        public String toString() {
            return "webroot@" + ip + " " + password + " " + ddl;
        }

        public String getLast() {
            return this.ip.split("\\.")[3];
        }
    }

    private static File[] getFiles(String path, String extension_name) {
        File dir = new File(path);
        if ((!dir.exists()) || (!dir.isDirectory())) {
            return new File[0];
        } else {
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(extension_name);
                }
            });
            return files;
        }
    }

    private static final Pattern table_pattern = Pattern.compile("<table class=3D\"table-flow\">([\\s\\S]*)</table>");

    private LinkedList<Server> parseEml(String path) throws Exception {
        String content = new OuterFileReader(path).read();
        Matcher table_matcher = table_pattern.matcher(content);
        LinkedList<Server> servers = new LinkedList<>();
        if (table_matcher.find()) {
            String table = table_matcher.group();
            Document doc = Jsoup.parse(table);
            Elements trs = doc.getElementsByTag("tr");
            for (Element tr : trs) {
                String tr_html = tr.html();
                if (tr_html.contains("webroot")) {
                    Elements tds = tr.getElementsByTag("td");
                    if (tds.size() == 3) {
                        continue;
                    }
                    String ip = tds.get(0).text();
                    String password = tds.get(2).text();
                    String datetime = tds.get(3).text().replace("= ", "");
                    Server server = new Server(ip, password, datetime);
                    servers.add(server);
                }
            }
        }
        return servers;
    }

    public LinkedList<Server> getServers(String dictionary_path) throws Exception {
        File[] emls = getFiles(dictionary_path, "eml");
        HashMap<String, Server> ss = new HashMap<>();
        for (File f : emls) {
            LinkedList<Server> servers_sub_list = parseEml(f.getAbsolutePath());
            servers_sub_list.forEach(server -> {
                String ip = server.getIp();
                if (!ss.containsKey(ip)) {
                    ss.put(ip, server);
                } else {
                    if (server.isAfter(ss.get(ip))) {
                        /**
                         * update the newer info
                         */
                        ss.put(ip, server);
                    }
                }
            });
        }
        LinkedList<Server> servers = new LinkedList<>();
        for (Map.Entry<String, Server> entry : ss.entrySet()) {
            servers.add(entry.getValue());
        }
        return servers;
    }
}
