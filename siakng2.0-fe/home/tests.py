from django.test import TestCase
from django.test import Client
from django.urls import resolve, reverse
from django.http import HttpRequest, response


class PengumumanTest(TestCase):
    def test_url_homepage_ada_url_dengan_HTTP_respon_yang_tepat(self):
        response = Client().get('/')
        self.assertEqual(response.status_code, 200)
        
    def test_url_homepage_menggunakan_HTML_yang_telah_ditentukan(self):
        response = Client().get('/')
        self.assertTemplateUsed(response,"home/index.html")

    # Homepage import necessary javascript
    # def test_pengisian_irs_contains_words(self):
    #     response = Client().get('/')
    #     html_response = response.content.decode('utf8')
    #     self.assertIn('''<script type="text/javascript" src="{% static 'home/index.js' %}"></script>''', html_response)