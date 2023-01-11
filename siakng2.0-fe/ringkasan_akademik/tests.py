from multiprocessing.connection import Client
from django.test import TestCase, Client
from .views import ringkasan_akademik

url = '/main/Academic/Summary'

class RingkasanAkademikTest(TestCase):
    def setUp(self):
        self.response = Client().get(url)

    def test_ringkasan_akademik_url_found(self):
        self.assertNotEquals(self.response.status_code, 404)

    def test_ringkasan_akademik_uses_ringkasan_akademik_views(self):
        self.assertEquals(self.response.resolver_match.func, ringkasan_akademik)

    def test_ringkasan_akademik_uses_correct_template(self):
        self.assertTemplateUsed(self.response, 'ringkasan-akademik.html')