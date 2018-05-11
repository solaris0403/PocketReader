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
        Document document = Jsoup.parse(shareBean.getHtml());
        shareBean.setTitle(document.title());
        shareBean.setDescription(document.outerHtml());
        shareBean.setDomain(RouterUtils.getHost(document.baseUri()));
        if (getSuccessor() != null) {
            getSuccessor().handleRequest(shareBean);
        } else {
            ShareHelper.processShareBean(shareBean);
        }
    }
}
