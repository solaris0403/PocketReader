package com.pocket.reader.core;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tony on 5/9/18.
 */

public class HtmlParse {

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
    /**
     * 得到网页中图片的地址
     */
    public static String getImgStr(String htmlStr) {
        List<String> pics = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        if (!pics.isEmpty()){
            return pics.get(0);
        }else{
            return "";
        }
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

    private static Document htmlParse(String string){
        Document document = null;
        try {
            document = Jsoup.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
}
