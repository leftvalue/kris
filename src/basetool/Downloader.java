package basetool;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;

public class Downloader {

    public boolean download(String path, String method, String url) {
        try {
            Connection.Response response =
                    Jsoup.connect(url)
                            .method(method.equals("GET") ? Connection.Method.GET : Connection.Method.POST)
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true).execute();
            System.out.println(response.contentType());
            FileOutputStream os = new FileOutputStream(new File(path));
            os.write(response.bodyAsBytes());
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
