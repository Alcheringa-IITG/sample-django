from restfblogin import views
from django.conf.urls import url
from django.urls import path
from rest_framework.authtoken import views as authviews

urlpatterns = [
    url(r'^fbregister/$', views.RegisterUser.as_view(), name='restfblogin-registeruser'),
]
