package tencent;

import model.AbstractRecord;

/**
 * @author linxi
 * www.leftvalue.top
 * ssh
 * Date 2018/7/24 7:46 PM
 */
public class ServerRecord extends AbstractRecord {
    private String ip;
    private String password;

    public ServerRecord(String ip, String password, String info) {
        super(info);
        this.ip = ip;
        this.password = password;
    }

    public ServerRecord(String ip, String password) {
        super("");
        this.ip = ip;
        this.password = password;
    }

    @Override
    public String toString() {
        return "webroot@" + ip;
    }

    public String getIp() {
        return ip;
    }

    public String getPassword() {
        return password;
    }

    public ServerRecord updatePassword(String password) {
        this.password = password;
        return this;
    }
}
