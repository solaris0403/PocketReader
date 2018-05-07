# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Bookmark(models.Model):
    # 链接
    url = models.CharField(max_length=128)
    # 标题
    title = models.CharField(max_length=64)
    # 创建时间
    time = models.DateTimeField(auto_now_add=True)
    # 图标
    icon = models.CharField(max_length=128)
    # 作者
    auth = models.CharField(max_length=32)
    # 类型 0:文章 1：图片 2：视频 3：音频
    type = models.IntegerField(default=0)
    # 读取状态 0：未读 1：已读 2：在读
    read_state = models.IntegerField(default=0)

    def __unicode__(self):
        return self.title
