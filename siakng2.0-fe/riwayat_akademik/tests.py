from multiprocessing.connection import Client
from django.test import TestCase, Client
from .views import riwayat_akademik

url = '/main/Academic/HistoryByTerm'

class RiwayatAkademikTest(TestCase):
    def setUp(self):
        self.response = Client().get(url)

    def test_riwayat_akademik_url_found(self):
        self.assertNotEquals(self.response.status_code, 404)

    def test_riwayat_akademik_uses_test_riwayat_akademik_views(self):
        self.assertEquals(self.response.resolver_match.func, riwayat_akademik)

    def test_riwayat_akademik_uses_correct_template(self):
        self.assertTemplateUsed(self.response, 'riwayat-akademik.html')