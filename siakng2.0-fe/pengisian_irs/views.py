from django.shortcuts import render

# Create your views here.
def isi_irs(request):
    return render(request, "pengisian_irs.html")

def drop_irs(request):
    return render(request, "drop_irs.html")

def add_irs(request):
    return render(request, "add_irs.html")
