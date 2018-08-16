package extensions.ding;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/16 4:37 PM
 */


import basetool.Port;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static spark.Spark.*;

/**
 * listen to a port
 * when request
 * notice run
 */
public class SimpleHttpPortListener extends Thread {
    private int port = 12345;
    private AbstractNotice notice = null;
    private static Executor threads_poll = Executors.newCachedThreadPool();

    public SimpleHttpPortListener() {
    }

    public SimpleHttpPortListener setPort(int port) {
        this.port = port;
        return this;
    }

    public SimpleHttpPortListener setNotice(AbstractNotice notice) {
        this.notice = notice;
        return this;
    }

    @Override
    public void run() {
        if (Port.isLocalPortUsing(port)) {
            System.out.println("localhost:" + port + " is using\nchange to localhost:" + (port + 1));
            port += 1;
            return;
        }
        port(port);
        System.out.println("listen on http://127.0.0.1:" + port + "/*");
        AbstractNotice local_notice = this.notice;
        get("/"
                , (req, res) -> {
                    local_notice.setMessage(req.uri());
                    threads_poll.execute(local_notice);
                    return "hello world";
                });
        notFound((req, res) -> {
            local_notice.setMessage(req.uri());
            threads_poll.execute(local_notice);
            return "hello world";
        });
    }

}

