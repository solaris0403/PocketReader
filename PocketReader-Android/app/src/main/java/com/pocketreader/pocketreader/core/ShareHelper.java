package com.pocketreader.pocketreader.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextUtils;

import com.pocketreader.pocketreader.PLog;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tony on 5/9/18.
 */

public class ShareHelper {
    private static final String TAG = "ShareHelper";
    public static final byte INVALID = -1;
    public static final byte SHARE_TEXT = 0;
    public static final byte SHARE_LINK = 1;
    public static final byte SHARE_IMAGE = 3;
    public static final byte SHARE_IMAGES = 4;
    public static final byte SHARE_FILE = 5;
    public static final byte SHARE_FILES = 6;

    public static byte getShareType(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        byte shareType = INVALID;
        if (Intent.ACTION_SEND.equals(action) && type != null) { //分享单个
            Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (type.startsWith("text/")) {//有可能是txt文件
                if (isFileType(intent)){
                    shareType = SHARE_FILE;
                }else{
                    shareType = linkMatcher(intent);
                }
            } else if ("message/rfc822".equals(type)){
                shareType = SHARE_TEXT;
            } else if (type.startsWith("image/")) {
//                if (BitmapUtil.isImageFile(UriUtils.getRealFilePath(AppContext.getContext(), fileUri))){
//                    shareType = SHARE_IMAGE;
//                }else{
//                    shareType = SHARE_FILE;
//                }
            } else{
//                if (BitmapUtil.isImageFile(UriUtils.getRealFilePath(AppContext.getContext(), fileUri))){
//                    shareType = SHARE_IMAGE;
//                }else{
//                    shareType = SHARE_FILE;
//                }
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {//分享多个
//            ArrayList<Uri> fileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//            if (type.startsWith("image/")) {
//                shareType = SHARE_IMAGES;
//                for (Uri uri : fileUris){
//                    if (!BitmapUtil.isImageFile(UriUtils.getRealFilePath(AppContext.getContext(), uri))){
//                        shareType = SHARE_FILES;
//                        return shareType;
//                    }
//                }
//            } else {
//                //也有可能是图片，但是type传入的为*/*，为了优化体验，需要做图片验证
//                for (Uri uri : fileUris){
//                    if (!BitmapUtil.isImageFile(UriUtils.getRealFilePath(AppContext.getContext(), uri))){
//                        //只要有一个不是图片，则判定为文件
//                        shareType = SHARE_FILES;
//                        return shareType;
//                    }
//                }
//                shareType = SHARE_IMAGES;
//            }
        }
        return shareType;
    }

    private static boolean isFileType(Intent intent){
        boolean isFile = false;
        Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (fileUri != null && !TextUtils.isEmpty(String.valueOf(fileUri))){
            isFile = true;
        }
        return isFile;
    }

    public static String getShareText(Intent intent){
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (TextUtils.isEmpty(sharedText)){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                Object bundleExt = bundle.get(Intent.EXTRA_TEXT);
                if (bundleExt != null){
                    if (bundleExt instanceof String){
                        sharedText = (String) bundleExt;
                    }else if (bundleExt instanceof Spannable){
                        Spannable spannable = (Spannable) bundleExt;
                        sharedText = spannable.toString();
                    }
                }
            }
        }
        return sharedText;
    }

    /**
     * 针对text进行link匹配<br/>
     * 由于link和url都是text，所以需要进行检查<br/>
     *
     * @param intent
     * @return
     */
    private static byte linkMatcher(Intent intent) {
        byte shareType = SHARE_TEXT;
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!TextUtils.isEmpty(sharedText)) {
            String title = intent.getStringExtra(Intent.EXTRA_SUBJECT);
            //如果url为空则从extra_text中查找有关的url
            String url = getUrlInContent(sharedText);
            if (TextUtils.isEmpty(title)) {
                shareType = SHARE_TEXT;
            }
            if (!TextUtils.isEmpty(url)) {
                try {
                    URL eUrl = new URL(url);
                    shareType = SHARE_LINK;
                } catch (Exception e1) {
                    shareType = SHARE_TEXT;
                }
            }
        }
        return shareType;
    }

    public static String getUrlInContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            String url = getFinalUrl(content);
            url = urlFilter(url);
            return url;
        }
        return null;
    }

    /**
     * 通过对content中分享的指定字符串进行拼接，来获取该页面
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

    private static String getFinalUrl(String content){
        PLog.d(TAG, content);
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
            if (link.contains("www.163.com/newsapp")){
                String newsCodeUrl = getNewsCodeUrl(content);
                if (!TextUtils.isEmpty(newsCodeUrl)){
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
        PLog.d(TAG, link);
        return link;
    }
    private static String urlFilter(String url){
        if (TextUtils.isEmpty(url)){
            return url;
        }
        String pattern = "[\u4e00-\u9fa5]";
        url =  url.replaceAll(pattern, "");
        return url;
    }
}
