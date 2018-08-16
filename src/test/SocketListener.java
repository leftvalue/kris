package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/16 4:31 PM
 */
public class SocketListener {
    private int port = 1122;
    private ServerSocket serverSocket;

    public SocketListener() throws Exception {
        serverSocket = new ServerSocket(port, 3);
        System.out.println("服务器启动!");
    }

    public void service() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("New connection accepted " +
                        socket.getInetAddress() + ":" + socket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SocketListener server = new SocketListener();
        Thread.sleep(3000);
        server.service();
    }

}
