from django.test import TestCase, Client
from django.urls import resolve
from .views import ringkasan

# Create your tests here.
url = '/main/CoursePlan/CoursePlanViewSummary'

class RingkasanTest(TestCase):
    def test_ringkasan_url_exist(self):
        response = Client().get(url)
        self.assertEqual(response.status_code, 200)

    def test_ringkasan_page_uses_ringkasan_views(self):
        found = resolve(url)
        self.assertEqual(found.func, ringkasan)

    def test_ringkasan_uses_correct_html(self):
        response = Client().get(url)
        self.assertTemplateUsed(response,"ringkasan_irs/ringkasan-irs.html")