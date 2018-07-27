package tencent;

import model.AbstractRecord;

/**
 * @author linxi
 * www.leftvalue.top
 * mysql
 * Date 2018/7/24 7:46 PM
 */
public class DatabaseRecord extends AbstractRecord {
    private String charset = "utf8";
    private String ip;
    private int port;
    private String username;
    private String password;
    private String dbname;

    public DatabaseRecord setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDbname() {
        return dbname;
    }

    @Override
    public String toString() {
        return "mysql -h" + ip + " -P" + port + " -u" + username + " -p" + password + " -A " + dbname + " --default-character-set=" + charset;
    }

    public DatabaseRecord(String ip, int port, String dbname, String username, String password, String info) {
        super(info);
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.dbname = dbname;
    }

    public DatabaseRecord(String ip, int port, String dbname, String username, String password) {
        super("");
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.dbname = dbname;
    }
}
