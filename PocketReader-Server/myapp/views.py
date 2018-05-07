# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import json

from django.core import serializers
from django.http import JsonResponse

# Create your views here.
from django.views.decorators.http import require_http_methods

from myapp import webutils
from myapp.models import Bookmark


@require_http_methods(["GET"])
def add(request):
    response = {}
    try:
        url = request.GET.get('url')
        title = str(webutils.get_web_title(url=url))
        book = Bookmark(url=url, title=title)
        book.save()
        response['msg'] = 'success'
        response['error_num'] = 0
    except Exception as e:
        response['msg'] = str(e)
        response['error_num'] = 1
    return JsonResponse(response)


@require_http_methods(["GET"])
def list(request):
    response = {}
    try:
        books = Bookmark.objects.filter()
        response['list'] = json.loads(serializers.serialize("json", books))
        response['msg'] = 'success'
        response['error_num'] = 0
    except Exception as e:
        response['msg'] = str(e)
        response['error_num'] = 1

    return JsonResponse(response)


@require_http_methods(["GET"])
def delete(request):
    response = {}
    try:
        book = Bookmark(id=request.GET.get('id'))
        book.delete()
        response['msg'] = 'success'
        response['error_num'] = 0
    except Exception as e:
        response['msg'] = str(e)
        response['error_num'] = 1
    return JsonResponse(response)


@require_http_methods(["GET"])
def update(request):
    response = {}
    try:
        param = request.GET
        print(param)
        # book = Bookmark(id=request.GET.get('id'))
        # book.delete()
        response['msg'] = 'success'
        response['error_num'] = 0
    except Exception as e:
        response['msg'] = str(e)
        response['error_num'] = 1
    return JsonResponse(response)
