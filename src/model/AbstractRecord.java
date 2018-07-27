package model;

import java.time.LocalDateTime;

/**
 * @author linxi
 * www.leftvalue.top
 * model
 * Date 2018/7/24 7:48 PM
 */
public class AbstractRecord {
    private String info;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public AbstractRecord(String info) {
        this.info = info;
        LocalDateTime now = LocalDateTime.now();
        this.created_at = now;
        this.updated_at = now;
    }

    public String getInfo() {
        return info;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    /*
     * 所有的更新都要调用该方法
     */
    public AbstractRecord reflesh() {
        this.updated_at = LocalDateTime.now();
        return this;
    }
}
