package model;

/**
 * @author linxi
 * www.leftvalue.top
 * model
 * Date 2018/7/27 10:58 AM
 * 用来存储一些其他的没有确定规则的内容
 */
public class Chip extends AbstractRecord {
    private String content;

    public Chip(String info) {
        super(info);
    }

    public String getContent() {
        return content;
    }
}
