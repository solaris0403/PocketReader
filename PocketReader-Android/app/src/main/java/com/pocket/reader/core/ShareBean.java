package com.pocket.reader.core;

/**
 * Created by tony on 5/11/18.
 */

public class ShareBean {
    public static final int TYPE_INVALID = -1;
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_LINK = 1;

    private String extra;//原始文本
    private int type;//分享进来的类型,文本，或者链接
    private String originalUrl;//原始地址
    private String redirectUrl;//重定向后的地址
    private String domain;//域名
    private String source;//来源
    private String title;//标题
    private String description;//描述
    private String html;//html dom
    private String icon;//该网站的图标，或者来源图标
    private String thumb;//第一张图片，用于展示

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "ShareBean{" +
                "extra='" + extra + '\'' +
                ", type=" + type +
                ", originalUrl='" + originalUrl + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", domain='" + domain + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", html='" + "---" + '\'' +
                ", icon='" + icon + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
