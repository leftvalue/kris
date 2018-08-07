package extensions;

import basetool.Colors;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.fusesource.jansi.Ansi;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/3 4:50 PM
 * A http proxy that allow u to tracert some important information and know where they come from
 * and it will print the curl command so that u can resend it to review the response
 */
public class Proxy {
    public static void main(String[] args) {
        new Proxy(8888, "以上分辨率访问本网站").listen();
    }

    private int port;
    private Pattern keyword_pattern;
    private boolean need_analyse;

    public Proxy(int port, String keyword_pattern_string) {
        this.port = port;
        keyword_pattern_string = keyword_pattern_string.trim();
        if (!keyword_pattern_string.equals("")) {
            try {
                this.keyword_pattern = Pattern.compile(keyword_pattern_string);
                need_analyse = true;
            } catch (PatternSyntaxException e) {
                System.out.println("PatternSyntaxException,run with no analyse mode");
                need_analyse = false;
            }
        } else {
            need_analyse = false;
        }
    }

    public void listen() {
        HttpProxyServer server =
                DefaultHttpProxyServer.bootstrap()
                        .withPort(port)
                        .withFiltersSource(new HttpFiltersSourceAdapter() {
                            @Override
                            public int getMaximumRequestBufferSizeInBytes() {
                                return 10 * 1024 * 1024;
                            }

                            @Override
                            public int getMaximumResponseBufferSizeInBytes() {
                                return 10 * 1024 * 1024;
                            }

                            @Override
                            public HttpFilters filterRequest(HttpRequest req, ChannelHandlerContext ct) {
                                String uri = req.getUri();
                                if (uri.startsWith("http://")) {
                                    /**
                                     * 暂时只能分析http 协议的请求,https 的就忽略了(但是会正常代理转发)
                                     */
                                    System.out.println(req.getUri() + " " + req.getMethod());
                                }
                                return new HttpFiltersAdapter(req) {
                                    @Override
                                    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                        return null;
                                    }

                                    @Override
                                    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
                                        return null;
                                    }

                                    @Override
                                    public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                        FullHttpResponse httpResponse = (FullHttpResponse) httpObject;
                                        if (need_analyse) {
                                            String content = httpResponse.content().toString(
                                                    this.getCharset(true)
                                            );
                                            Matcher matcher = keyword_pattern.matcher(content);
                                            if (matcher.find()) {
                                                System.out.println("[ FOUND " + keyword_pattern.pattern() + " ]" + req.getUri());
                                                System.out.println(Colors.getCS(Ansi.Color.YELLOW, parseCURLCommand(req)));
                                            }
                                        }
                                        return super.serverToProxyResponse(httpObject);
                                    }

                                    @Override
                                    public HttpObject proxyToClientResponse(HttpObject httpObject) {
                                        return super.proxyToClientResponse(httpObject);
                                    }

                                    /**
                                     * get charset of response
                                     */
                                    public Charset getCharset(boolean defaultCharset) {
                                        //check for Content-Type header
                                        if (HttpHeaders.getHeader(originalRequest, HttpHeaders.Names.CONTENT_TYPE) != null) {
                                            ;
                                            //if Content-Type contains a charset specification
                                            if (HttpHeaders.getHeader(originalRequest, HttpHeaders.Names.CONTENT_TYPE).matches(".*charset=.*")) {
                                                //extract charset value through regex
                                                Matcher matcher = Pattern.compile("charset=(.*?)(?:,|$)").matcher(
                                                        HttpHeaders.getHeader(originalRequest, HttpHeaders.Names.CONTENT_TYPE)
                                                );

                                                if (matcher.find()) {
                                                    return Charset.forName(matcher.group(1));
                                                }
                                            }
                                        }
                                        return (defaultCharset ? Charset.defaultCharset() : null);
                                    }
                                };
                            }
                        }).start();
        System.out.println("Proxy listen " + server.getListenAddress());
    }

    /**
     * parse req into curl command so that u can easily resend it with terminal
     *
     * @param req
     * @return
     */
    static String parseCURLCommand(HttpRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("curl '");
        String url = req.getUri();
        sb.append(url);
        /**
         * remember to add space before \\n
         */
        sb.append("' \\\n");
        /**
         * 其实在 Curl 里不需要把 cookie 单独拎出来
         */
        HttpHeaders headers = req.headers();
        for (Map.Entry<String, String> header : headers.entries()) {
            sb.append("--header '").append(header.getKey()).append("=").append(header.getValue()).append("' \\\n");
        }
        HttpMethod method = req.getMethod();
        if ("POST".equals(method.name())) {
            sb.append("--data '");
            HttpPostRequestDecoder d = new HttpPostRequestDecoder(req);
            List<InterfaceHttpData> datas = d.getBodyHttpDatas();
            for (InterfaceHttpData data : datas) {
                String[] attribute = getPostData(data);
                sb.append(attribute[0] + "=" + attribute[1] + "&");
            }
            sb.append("'\\\n");
        }
        sb.append("--compressed");
        return sb.toString();
    }

    /**
     * get post data from interface_http_data
     *
     * @param data
     * @return
     */
    private static String[] getPostData(InterfaceHttpData data) {
        //Mixed: execution=e1s3
        String kv = data.toString();
        kv = kv.substring("Mixed: ".length());
        String[] k_v_array = kv.split("=");
        return k_v_array;
    }
}
