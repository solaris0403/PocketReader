package com.pocket.reader.core;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.litesuits.android.async.AsyncTask;
import com.pocket.reader.AppContext;
import com.pocket.reader.event.MessageEvent;
import com.pocket.reader.model.dao.LinkDao;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;

/**
 * 对分享的内容依次进行处理：
 * IntentHandler：intent解析
 * TextHandler：文本解析
 * LinkHandler：链接处理
 * HtmlHandler：对文档对象处理
 * 最终生成一个ShareBean对象
 */

public class ShareHelper {
    private static final String TAG = "ShareHelper";

    public static void processIntent(Intent intent) {
        IntentHandler intentHandler = new IntentHandler();
        TextHandler textHandler = new TextHandler();
        LinkHandler linkHandler = new LinkHandler();
        HtmlHandler htmlHandler = new HtmlHandler();
        intentHandler.setSuccessor(textHandler);
        textHandler.setSuccessor(linkHandler);
        linkHandler.setSuccessor(htmlHandler);
        intentHandler.handleRequest(intent);
    }

    /**
     * 对解析之后的share进行处理
     * 1. 无效
     * 2. 文本
     * 3. 链接
     *
     * @param shareBean
     */
    public static void processShareBean(ShareBean shareBean) {
        MessageEvent event = new MessageEvent(MessageEvent.TYPE_PROCESS_LINK);
        event.setMessage(shareBean.toString());
        EventBus.getDefault().post(event);
        Log.d(TAG, "processShareBean:" + shareBean.toString());
        switch (shareBean.getType()) {
            case ShareBean.TYPE_INVALID:
                Toast.makeText(AppContext.getContext(), "资源无效", Toast.LENGTH_SHORT).show();
                break;
            case ShareBean.TYPE_TEXT:

                break;
            case ShareBean.TYPE_LINK:
                LinkDao.saveLink(shareBean, new LinkDao.OnLinkListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
            default:
                break;
        }
    }

    public interface IconCallback {
        void onStart();

        void onFinish(ShareBean shareBean);
    }

    /**
     * 获取链接图标
     * 从流中读取apple-touch-icon/shortcut icon
     *
     * @return
     */
    public static AsyncTask getHtml(final ShareBean shareBean, final IconCallback iconCallback) {
        AsyncTask<ShareBean, Void, ShareBean> asyncTask = new AsyncTask<ShareBean, Void, ShareBean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (iconCallback != null) {
                    iconCallback.onStart();
                }
            }

            @Override
            protected ShareBean doInBackground(ShareBean... params) {
                ShareBean shareLinkBean = params[0];
                String html = "";
                try {
                    html = Jsoup.parse(new URL(shareLinkBean.getOriginalUrl()), 30000).html();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                shareLinkBean.setHtml(html);
                return shareLinkBean;
            }

            @Override
            protected void onPostExecute(ShareBean shareLinkBean) {
                if (iconCallback != null) {
                    iconCallback.onFinish(shareLinkBean);
                }
            }
        };
        asyncTask.execute(shareBean);
        return asyncTask;
    }
}
