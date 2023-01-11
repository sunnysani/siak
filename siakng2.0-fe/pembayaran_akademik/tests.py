from django.test import TestCase, Client
from .views import pembayaran_akademik

url = '/main/Academic/Payment'

class PembayaranAkademikTest(TestCase):
    def setUp(self):
        self.response = Client().get(url)

    def test_pembayaran_akademik_url_found(self):
        self.assertNotEquals(self.response.status_code, 404)
        
    def test_pembayaran_akademik_uses_pembayaran_akademik_views(self):
        self.assertEquals(self.response.resolver_match.func, pembayaran_akademik)
        
    def test_pembayaran_akademik_uses_correct_template(self):
        self.assertTemplateUsed(self.response, 'pembayaran-akademik.html')
        
    def test_pembayaran_akademik_template_inherits_base_template(self):
        self.assertTemplateUsed(self.response, 'base.html')