from django.urls import path
from . import views

app_name = 'ringkasan_akademik'

urlpatterns = [
    path('main/Academic/Summary', views.ringkasan_akademik, name = 'ringkasan_akademik')
]
