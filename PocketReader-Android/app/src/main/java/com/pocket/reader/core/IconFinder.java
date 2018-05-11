package com.pocket.reader.core;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.webkit.WebSettings;

import com.pocket.reader.AppContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/**
 * Descriptions
 *
 * @author luguo3000
 * @version 2014-6-5
 * @since JDK1.6
 */
public class IconFinder {

    private static final Pattern[] ICON_PATTERNS = new Pattern[]{
            Pattern.compile("rel=[\"']shortcut icon[\"'][^\r\n>]+?((?<=href=[\"']).+?(?=[\"']))"),
            Pattern.compile("((?<=href=[\"']).+?(?=[\"']))[^\r\n<]+?rel=[\"']shortcut icon[\"']")};
    private static final Pattern HEAD_END_PATTERN = Pattern.compile("</head>");

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        Set<String> urlSet = getUrls("http://www.hao123.com/");
        for (String urlString : urlSet) {
            System.out.println(urlString);
            System.out.println(getIconUrlString(urlString));
        }

        System.out.println(urlSet.size());
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    // 获取稳定url
    private static String getFinalUrl(String urlString) {
        HttpURLConnection connection = null;
        try {
            connection = getConnection(urlString);
            connection.connect();

            // 是否跳转，若跳转则跟踪到跳转页面
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM
                    || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                String location = connection.getHeaderField("Location");
                if (!location.contains("http")) {
                    location = urlString + "/" + location;
                }
                return location;
            }
        } catch (Exception e) {
            System.err.println("获取跳转链接超时，返回原链接" + urlString);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return urlString;
    }

    // 获取Icon地址
    public static String getIconUrlString(String urlString) throws MalformedURLException {
        urlString = getFinalUrl(urlString);
        URL url = new URL(urlString);
        String iconUrl = url.getProtocol() + "://" + url.getHost() + "/favicon.ico";// 保证从域名根路径搜索
        if (hasRootIcon(iconUrl)) {
            return iconUrl;
        }
        return getIconUrlByRegex(urlString);
    }

    // 判断在根目录下是否有Icon
    private static boolean hasRootIcon(String urlString) {
        HttpURLConnection connection = null;
        try {
            connection = getConnection(urlString);
            connection.connect();
            return HttpURLConnection.HTTP_OK == connection.getResponseCode() && connection.getContentLength() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    // 从html中获取Icon地址
    private static String getIconUrlByRegex(String urlString) {
        try {
            String headString = getHead(urlString);
            for (Pattern iconPattern : ICON_PATTERNS) {
                Matcher matcher = iconPattern.matcher(headString);

                if (matcher.find()) {
                    String iconUrl = matcher.group(1);
                    if (iconUrl.contains("http"))
                        return iconUrl;

                    if (iconUrl.charAt(0) == '/') {//判断是否为相对路径或根路径
                        URL url = new URL(urlString);
                        iconUrl = url.getProtocol() + "://" + url.getHost() + iconUrl;
                    } else {
                        iconUrl = urlString + "/" + iconUrl;
                    }
                    return iconUrl;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 爬取一级域名
    private static Set<String> getUrls(String urlString) {
        Set<String> urlSet = new HashSet<String>();
        Pattern pattern = Pattern.compile("(http|https)://www\\..+?\\.(aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel|[a-z]{2})");
        Matcher matcher = pattern.matcher(getHtml(urlString));
        while (matcher.find()) {
            urlSet.add(matcher.group());
        }
        return urlSet;
    }

    // 获取截止到head尾标签的文本
    private static final String getHead(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = getConnection(urlString);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;
            StringBuilder headBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                Matcher matcher = HEAD_END_PATTERN.matcher(line);
                if (matcher.find()) {
                    break;
                }
                headBuilder.append(line);
            }
            return headBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取html页面文本
    private static final String getHtml(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            connection = getConnection(urlString);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            StringBuilder htmlBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                htmlBuilder.append(line);
            }
            return htmlBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (connection != null)
                    connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    public static SSLContext sslContextForTrustedCertificates() {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new IconFinder.miTM();
        trustAllCerts[0] = tm;
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, null);
            //javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (KeyManagementException e) {
            e.printStackTrace();
        }finally {
            return sc;
        }
    }

    public static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }
        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
    private static SSLSocketFactory mSSLSocketFactory = sslContextForTrustedCertificates().getSocketFactory();


    // 获取一个连接
    private static HttpURLConnection getConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        String userAgent = System.getProperty("http.agent");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            userAgent = WebSettings.getDefaultUserAgent(AppContext.getContext());
        }
        connection.setRequestProperty("User-Agent",userAgent);

        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(mSSLSocketFactory);
            ((HttpsURLConnection)connection).setHostnameVerifier((DO_NOT_VERIFY));
        }

        return connection;
    }


    public static String getIcon(String pageSource, String pageUrl) {
        if (pageSource == null || pageUrl == null){
            return null;
        }
        Document document = htmlParse(pageSource);
        if (document == null) {
            //Log.e("FindIcon","parse document is null");
            return null;
        }
        String iconUrl = getIcon("apple-touch-icon", document, true);
        if(iconUrl == null){
            //Log.e("FindIcon","find apple touch icon is null");
        }

        if (TextUtils.isEmpty(iconUrl)) {
            iconUrl = getIcon("icon", document, false);
            if(iconUrl == null){
                //Log.e("FindIcon","find icon is null");
            }
        }

        if (TextUtils.isEmpty(iconUrl)) {
            iconUrl = getIcon("shortcut icon", document, false);
            if(iconUrl == null){
                //Log.e("FindIcon","find shortcut icon is null");
            }
        }

        if(TextUtils.isEmpty(iconUrl)) {
            //Log.e("FindIcon","try pattern find the first picture");
            //parse by myself
            Pattern iconPattern = Pattern.compile("(http|https)://.*.(jpg|png|icon|jpeg)");
            Matcher matcher = iconPattern.matcher(pageSource);
            if (matcher.find()) {
                iconUrl = matcher.group();
                //Log.e("FindIcon","elements size is zero and html is " + iconUrl);
            }else{
                //Log.e("FindIcon","try pattern find the first picture4");
            }
        }

        //相对路径
        if (!TextUtils.isEmpty(iconUrl) && !URLUtil.isNetworkUrl(iconUrl)) {
            try {
                URL myUrl = new URL(pageUrl);
                if (iconUrl.startsWith("//")) {
                    iconUrl = myUrl.getProtocol() + ":" + iconUrl;
                } else if (iconUrl.charAt(0) == '/') {
                    iconUrl = myUrl.getProtocol() + "://" + myUrl.getHost() + iconUrl;
                } else {
                    String path = myUrl.getPath();
                    iconUrl = myUrl.getProtocol() + "://" + myUrl.getHost() + path + (path.endsWith("/") ? "" : "/") + iconUrl;
                }
            } catch (MalformedURLException e) {
                iconUrl = null;
                e.printStackTrace();
            }
        }
        return iconUrl;
    }

    public static String getTitle(String pageSource) {
        if (pageSource == null){
            return null;
        }
        Document document = htmlParse(pageSource);
        if (document == null){
            return null;
        }
        return document.title();
    }

    public static String getDescription(String pageSource){
        Document document = htmlParse(pageSource);
        if (document != null){
            Elements links = document.getElementsByTag("meta");
            for (Element link : links) {
                if ("description".equalsIgnoreCase(link.attr("name"))){
                    return link.attr("content");
                }
            }
        }
        return null;
    }

    private static Document htmlParse(String string){
        Document document = null;
        try {
            document = Jsoup.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    private static String getIcon(String value, Document document, boolean isPrefix) {
        if (!TextUtils.isEmpty(value) && document != null) {
            Elements elements = isPrefix ? document.getElementsByAttributeValueStarting("rel", value)
                    : document.getElementsByAttributeValue("rel", value);
            if(elements.size() > 0){
                for (Element element : elements) {
                    String href = element.attr("href");
                    Log.e("FindIcon","href is " + href);
                    if (!TextUtils.isEmpty(href) && !href.endsWith(".svg")) {
                        return href;
                    }
                }
            }
        }
        return null;
    }

    public static String[] getUrlAndHtmlHead(String url){
        return getUrlAndHtmlHead(url, null, false, true);
    }

    public static String[] getUrlAndHtmlHead(String url, boolean only200){
        return getUrlAndHtmlHead(url, null, only200, true);
    }

    public static String[] getUrlAndHtmlHead(String url, boolean only200, boolean onlyHead){
        return getUrlAndHtmlHead(url, null, only200, onlyHead);
    }

    /**
     * 获取请求头里面的charset
     * @param contentType
     * @return
     */
    private static String getHeaderCharset(String contentType){
        String charset = null;
        if (!TextUtils.isEmpty(contentType)){
            int m = contentType.indexOf("charset=");
            if (m != -1) {
                charset = contentType.substring(m + 8).replace("]", "");
            }
        }
        if (!TextUtils.isEmpty(charset) && Charset.isSupported(charset)){
            return charset;
        }else{
            return "UTF-8";
        }
    }

    /**
     * 获取网页中的编码
     * @param pageSource
     * @return
     */
    private static String getWebCharset(String pageSource){
        String charset = null;
        Document document = htmlParse(pageSource);
        if (document != null){
            Elements links = document.getElementsByTag("meta");
            for (Element link : links) {
                if ("Content-Type".equalsIgnoreCase(link.attr("http-equiv"))){
                    String contentType = link.attr("content");
                    if (!TextUtils.isEmpty(contentType)){
                        int m = contentType.indexOf("charset=");
                        if (m != -1) {
                            charset = contentType.substring(m + 8).replace("]", "");
                        }
                    }
                    break;
                }
            }
        }
        if (!TextUtils.isEmpty(charset) && Charset.isSupported(charset)){
            return charset;
        }else{
            return "";
        }
    }

    public static String[] getUrlAndHtmlHead(String url, String charset, boolean only200, boolean onlyHead){
        HttpURLConnection connection = null;
        String[] web = new String[2];
        StringBuilder stringBuilder = new StringBuilder();
        final String headPattern = "<head>[\\s\\S]+?<\\/?head>";
        final Pattern r = Pattern.compile(headPattern);
        BufferedReader buffer = null;
        try {
            connection = getConnection(url);

            //重定向
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                String location = connection.getHeaderField("Location");
                if (!location.contains("http")) {
                    location = url + "/" + location;
                }
                return getUrlAndHtmlHead(location, charset, only200, onlyHead);
            }

            web[0] = connection.getURL().toString();

            //检查返回是不是200
            if (only200 && connection.getResponseCode()!= HttpURLConnection.HTTP_OK){
                return web;
            }

            //没有指定charset, 获取header编码格式
            if (TextUtils.isEmpty(charset)){
                charset = getHeaderCharset(connection.getContentType());
            }

            //顺序读取
            InputStreamReader in = new InputStreamReader(connection.getInputStream(), Charset.forName(charset));
            buffer = new BufferedReader(in);
            String inputLine;
            while (((inputLine = buffer.readLine()) != null)){
                //有可能web编码和head里面的不一致，所以需要重新获取
                if (inputLine.contains("charset=")){
                    String webCharset = getWebCharset(inputLine);
                    //网页和出进来的不一致 重新指定charset
                    if (!TextUtils.isEmpty(webCharset) && !webCharset.equalsIgnoreCase(charset)){
                        return getUrlAndHtmlHead(web[0], webCharset, only200, onlyHead);
                    }
                }
                stringBuilder.append(inputLine).append("\n");
                if (onlyHead){
                    Matcher m = r.matcher(stringBuilder);
                    if (m.find()){
                        break;
                    }
                }
            }
            String finalUrl = web[0];
            if ("http".equals(RouterUtils.getScheme(finalUrl)) && TextUtils.isEmpty(inputLine)){
                finalUrl = finalUrl.replaceFirst("http", "https");
                return getUrlAndHtmlHead(finalUrl, charset, only200, onlyHead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        web[1] = stringBuilder.toString();
        return web;
    }
}
