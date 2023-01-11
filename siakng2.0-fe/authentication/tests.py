from django.test import TestCase, Client, LiveServerTestCase
from django.urls import resolve
from .views import auth

import time

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver import DesiredCapabilities
from selenium.webdriver.chrome.options import Options

# Create your tests here.
url = '/main/authentication'

class AuthenticationTest(TestCase):
    def test_auth_url_exist(self):
        response = Client().get(url)
        self.assertEqual(response.status_code, 200)

    def test_auth_page_uses_auth_views(self):
        found = resolve(url)
        self.assertEqual(found.func, auth)

    def test_auth_uses_correct_html(self):
        response = Client().get(url)
        self.assertTemplateUsed(response,"loginpage.html")

class AuthenticationFuncTest(LiveServerTestCase):
    def setUp(self):
        chrome_options = Options()
        chrome_options.add_argument('--dns-prefetch-disable')
        chrome_options.add_argument('--no-sandbox')
        chrome_options.add_argument('--headless')
        chrome_options.add_argument('disable-gpu')

        self.selenium = webdriver.Chrome('./chromedriver', chrome_options=chrome_options)
        self.selenium.get(self.live_server_url + url)
        super(AuthenticationFuncTest, self).setUp()

    def test_modal_works(self):
        selenium = self.selenium
        time.sleep(3)

        button = selenium.find_element_by_id("modalButton")
        button.click()
        time.sleep(3)

        self.assertIn('No. Tel. Fakultas', selenium.page_source)

    def tearDown(self):
        self.selenium.quit()
        super(AuthenticationFuncTest, self).tearDown()
