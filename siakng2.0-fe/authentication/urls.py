from unicodedata import name
from django.urls import path
from . import views

app_name = 'authentication'

urlpatterns = [
    path('main/authentication', views.auth, name='auth')
]
