from django.test import TestCase, Client
from django.urls import resolve

url = '/main/CoursePlan/CoursePlanViewCheck'

class PengecekanIrsTest(TestCase):

    def setUp(self):
        self.response = Client().get(url)
    
    def test_pengecekan_irs_url_exist(self):
        self.assertEqual(self.response.status_code, 200)
    
    def test_pengecekan_irs_use_correct_template(self):
        self.assertTemplateUsed(self.response, "pengecekan_irs/pengecekan-irs.html")

    def test_pengecekan_irs_contain_words(self):
        html_response = self.response.content.decode('utf8')
        self.assertIn('Pengecekan', html_response)
        self.assertIn('SKS', html_response)
        self.assertIn('Jadwal', html_response)
        self.assertIn('Kapasitas Kelas', html_response)
        self.assertIn('Prasyarat', html_response)
