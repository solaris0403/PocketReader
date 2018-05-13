package com.pocket.reader.core;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by tony on 5/11/18.
 * 对传进来的文档对象模型解析，结果是ShareBean
 */

public class HtmlHandler extends AbsHandler {
    @Override
    public void handleRequest(ShareBean shareBean) {
        Log.d(TAG, "HtmlHandler handleRequest");
        String html = shareBean.getHtml();
        Document document = Jsoup.parse(shareBean.getHtml());
        shareBean.setTitle(document.title());//ok
        shareBean.setThumb(HtmlParse.getImgStr(html));//ok
        shareBean.setIcon(HtmlParse.getIcon(html, shareBean.getOriginalUrl()));//ok
        shareBean.setDescription(HtmlParse.getDescription(html));//ok
        shareBean.setHost(RouterUtils.getHost(shareBean.getOriginalUrl()));//ok
        if (getSuccessor() != null) {
            getSuccessor().handleRequest(shareBean);
        } else {
            ShareHelper.processShareBean(shareBean);
        }
    }
}
