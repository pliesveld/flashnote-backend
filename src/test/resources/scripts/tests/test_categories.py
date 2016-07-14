import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools
import os


try:
    from .settings import *
    from .test_base import BaseTestCase, assert_2xx
    from .routes import CAT_RESOURCE, CAT_ROOT_RESOURCE, CAT_SEARCH_RESOURCE
except SystemError:
    from test_base import BaseTestCase
    from settings import *

class CategoryTest(BaseTestCase):

    def setUp(self):
        super().setUp()

    def testResource(self):
        self.url = CAT_ROOT_RESOURCE
        req = self.request()
        r = req(hooks=assert_2xx)
        self.assertEqual(r.status_code, 200)

    def testCategoriesList(self):
        self.url = CAT_RESOURCE
        self.query = {'query' : 'Software'}
        req = self.request()
        r = req()
        self.assertEqual(r.status_code, 200)

    def testCategoriesQuery(self):
        self.url = CAT_SEARCH_RESOURCE
        self.query = {'query' : 'SUB'}
        req = self.request()
        r = req()
        self.assertEqual(r.status_code, 200)
        json = r.json()
        self.assertIn('content', json)


if __name__ == '__main__':
    unittest.main()
