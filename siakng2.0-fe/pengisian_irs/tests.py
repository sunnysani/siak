from django.test import TestCase, Client
from django.urls import resolve

# Create your tests here.
base_html = "base.html"
navbar_htlm = "navbar.html"

class PengisianIrsTest(TestCase):
	def setUp(self):
		self.response = Client().get('/main/CoursePlan/CoursePlanEdit')

	def test_pengisian_irs_url_exist(self):
		self.assertEqual(self.response.status_code, 200)

	def test_pengisian_irs_use_template(self):
		self.assertTemplateUsed(self.response,"pengisian_irs.html")

	def test_pengisian_irs_contains_words(self):
		html_response = self.response.content.decode('utf8')
		self.assertIn('Pengisian IRS', html_response)

	def test_pengisian_irs_use_navbar_template(self):
		self.assertTemplateUsed(self.response, base_html)
		self.assertTemplateUsed(self.response, navbar_htlm)

class DropIrsTest(TestCase):
	def setUp(self):
		self.response = Client().get('/main/CoursePlan/CoursePlanDrop')
		
	def test_drop_irs_url_exist(self):
		self.assertEqual(self.response.status_code, 200)

	def test_drop_irs_use_template(self):
		self.assertTemplateUsed(self.response,"drop_irs.html")

	def test_drop_irs_contains_words(self):
		html_response = self.response.content.decode('utf8')
		self.assertIn('Drop IRS', html_response)

	def test_drop_irs_use_navbar_template(self):
		self.assertTemplateUsed(self.response, base_html)
		self.assertTemplateUsed(self.response, navbar_htlm)

class AddIrsTest(TestCase):
	
	def setUp(self):
		self.response = Client().get('/main/CoursePlan/CoursePlanAdd')
	
	def test_add_irs_url_exist(self):
		self.assertEqual(self.response.status_code, 200)
	
	def test_add_irs_use_template(self):
		self.assertTemplateUsed(self.response,"add_irs.html")

	def test_add_irs_contains_words(self):
		html_response = self.response.content.decode('utf8')
		self.assertIn('Add IRS', html_response)

	def test_add_irs_use_navbar_template(self):
		self.assertTemplateUsed(self.response,"base.html")
		self.assertTemplateUsed(self.response,"navbar.html")


