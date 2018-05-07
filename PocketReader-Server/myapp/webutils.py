#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 4/28/18 2:54 PM
# @Author  : Tony Cao
# @File    : webutils.py

'''''
功能：抽取指定url的页面内容中的title
'''

import urllib
from bs4 import BeautifulSoup


def get_web_title(url):
    html = urllib.request.urlopen(url).read()
    soup = BeautifulSoup(html, "lxml")
    title = soup.title
    return title


get_web_title("http://www.baidu.com")
