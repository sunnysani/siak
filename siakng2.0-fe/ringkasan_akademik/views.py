import http
from django.http import HttpResponse
from django.shortcuts import render

def ringkasan_akademik(request):
    return render(request, 'ringkasan-akademik.html')