from django.urls import path
from . import views

app_name = 'pengisian_irs'

urlpatterns = [
    path('main/CoursePlan/CoursePlanEdit', views.isi_irs, name='isi_irs'),
    path('main/CoursePlan/CoursePlanDrop', views.drop_irs, name='drop_irs'),
    path('main/CoursePlan/CoursePlanAdd', views.add_irs, name='add_irs'),
]
