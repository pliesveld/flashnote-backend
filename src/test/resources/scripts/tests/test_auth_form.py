import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools

try:
    from .settings import URL
except SystemError:
        from settings import URL


class AuthBaseTest(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        print("setup-class",file=sys.stderr)

    @classmethod
    def tearDownClass(cls):
        print("tear-down-class",file=sys.stderr)

    def setUp(self):
        print("setup-test ",file=sys.stderr)

    def tearDown(self):
        print("tear-down-test",file=sys.stderr)





class AuthTest(AuthBaseTest):
    ANON_RESOURCE = '/anon/Principal'
    USER_RESOURCE = '/auth/Principal'
    ADMIN_RESOURCE = '/admin/Principal'

    LOGIN = '/login'

    def testAnonAccess(self):
        with requests.Session() as s:
            reqBody = {'username':'student@example.com', 'password':'password'}
            r = s.request('POST',URL + self.LOGIN, data=reqBody)
            print(r.text)
            print(r.cookies)

            
            url = URL + self.USER_RESOURCE
            r = s.request('GET',url)
            print(r.text)
            self.assertEqual(r.status_code,200)
     

    def testNoAccess(self):
        with requests.Session() as s:
            url = URL + self.USER_RESOURCE
            r = s.request('GET',url)
            print(r.text)
            self.assertEqual(r.status_code,200)
     




if __name__ == '__main__':
    unittest.main()
