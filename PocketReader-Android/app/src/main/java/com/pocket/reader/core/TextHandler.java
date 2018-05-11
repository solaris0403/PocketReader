package com.pocket.reader.core;

import android.text.TextUtils;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tony on 5/11/18.
 * 处理传进来的文本，核心是对文本进行处理，结果是文本或者链接
 */

public class TextHandler extends AbsHandler {
    @Override
    public void handleRequest(ShareBean shareBean) {
        Log.d(TAG, "TextHandler handleRequest");
        if (!TextUtils.isEmpty(shareBean.getExtra())) {
            String url = getUrlInContent(shareBean.getExtra());
            if (TextUtils.isEmpty(url)) {
                shareBean.setType(ShareBean.TYPE_TEXT);
            } else {
                try {
                    URL eUrl = new URL(url);
                    shareBean.setType(ShareBean.TYPE_LINK);
                    shareBean.setOriginalUrl(url);
                    if (getSuccessor() != null) {
                        getSuccessor().handleRequest(shareBean);
                    } else {
                        ShareHelper.processShareBean(shareBean);
                    }
                } catch (Exception e1) {
                    shareBean.setType(ShareBean.TYPE_TEXT);
                    ShareHelper.processShareBean(shareBean);
                }
            }
        } else {
            ShareHelper.processShareBean(shareBean);
        }
    }

    public String getUrlInContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            String url = getFinalUrl(content);
            url = urlFilter(url);
            return url;
        }
        return null;
    }

    private String getFinalUrl(String content) {
        final String regex = "[a-zA-z]+:\\/\\/[^\\s]*";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(content);
        List<String> urls = new ArrayList<>();
        while (matcher.find()) {
            urls.add(matcher.group(0));
        }
        String link;
        if (urls.size() < 1) {
            link = null;
        } else if (urls.size() == 1) {
            link = urls.get(0);
            if (link.contains("www.163.com/newsapp")) {
                String newsCodeUrl = getNewsCodeUrl(content);
                if (!TextUtils.isEmpty(newsCodeUrl)) {
                    link = newsCodeUrl;
                }
            }
        } else {
            link = urls.get(0);
            for (String url : urls) {
                if (content.contains("www.163.com/newsapp")) {
                    continue;
                } else {
                    link = url;
                    break;
                }
            }
        }
        return link;
    }

    private String urlFilter(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        String pattern = "[\u4e00-\u9fa5]";
        url = url.replaceAll(pattern, "");
        return url;
    }

    /**
     * 通过对content中分享的指定字符串进行拼接，来获取该页面
     *
     * @param content
     * @return
     */
    private static String getNewsCodeUrl(String content) {
        String url = null;
        final Pattern pattern = Pattern.compile("\\n[\\d\\w]+\\n", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String newsCode = matcher.group(0).trim();
            url = "https://c.m.163.com/news/a/" + newsCode + ".html?spss=newsapp";
        }
        return url;
    }
}
