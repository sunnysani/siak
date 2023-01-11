from django.urls import path
from . import views

app_name = 'riwayat_akademik'

urlpatterns = [
    path('main/Academic/HistoryByTerm', views.riwayat_akademik, name = 'riwayat_akademik')
]
