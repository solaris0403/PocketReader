package com.pocket.reader.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by tony on 5/14/18.
 */

@DatabaseTable(tableName = "tab_link")
public class LinkTable {
    @DatabaseField(columnName = "objectId")
    public String objectId;
    @DatabaseField(columnName = "url")
    public String url;
    @DatabaseField(columnName = "title")
    public String title;
    @DatabaseField(columnName = "thumb")
    public String thumb;
    @DatabaseField(columnName = "source")
    public String source;
    @DatabaseField(columnName = "icon")
    public String icon;
    @DatabaseField(columnName = "content")
    public String content;
    @DatabaseField(columnName = "category")
    public int category;
}
