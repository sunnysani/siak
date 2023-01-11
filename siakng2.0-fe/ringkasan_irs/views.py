from django.shortcuts import render

# Create your views here.
def ringkasan(request):
    return render(request, 'ringkasan_irs/ringkasan-irs.html')