from django.urls import path
from . import views

app_name = 'pembayaran_akademik'

urlpatterns = [
    path('main/Academic/Payment', views.pembayaran_akademik, name = 'pembayaran_akademik')
]
