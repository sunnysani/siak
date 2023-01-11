from django.urls import path
from . import views

app_name = 'pengecekan_irs'

urlpatterns = [
	path('main/CoursePlan/CoursePlanViewCheck', views.pengecekan_irs, name="pengecekan_irs"),
]
