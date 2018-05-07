#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 4/27/18 1:36 PM
# @Author  : Tony Cao
# @File    : urls.py

from django.conf.urls import url

from myapp import views

urlpatterns = [
    url(r'add$', views.add),
    url(r'list$', views.list),
    url(r'delete$', views.delete),
    url(r'update$', views.update),
]
