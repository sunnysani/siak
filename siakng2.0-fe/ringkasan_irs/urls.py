from django.urls import path
from . import views

app_name = 'ringkasan_irs'

urlpatterns = [
	path('main/CoursePlan/CoursePlanViewSummary', views.ringkasan, name="ringkasan"),
]
