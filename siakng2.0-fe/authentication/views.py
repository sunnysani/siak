from django.shortcuts import render
from django.views.decorators.http import require_GET

# Create your views here.
@require_GET
def auth(request):
    return render(request, "loginpage.html")
