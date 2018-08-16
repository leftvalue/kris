package extensions.ding;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/16 3:52 PM
 */
public abstract class AbstractNotice implements Runnable {
    protected String message = "PING";

    public AbstractNotice setMessage(String msg) {
        this.message = message;
        return this;
    }
}
