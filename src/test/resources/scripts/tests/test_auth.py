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





class AnonTest(AuthBaseTest):
    auth_token=('student@example.com','password')
    ANON_RESOURCE = '/anon/Principal'
    USER_RESOURCE = '/auth/Principal'
    ADMIN_RESOURCE = '/admin/Principal'

    def testAnonAccess(self):
        with requests.Session() as s:
            url = URL + self.ANON_RESOURCE
            r = s.request('GET',url)
            self.assertEqual(r.status_code,200)
     
    def testUserAccess(self):
        with requests.Session() as s:
            url = URL + self.USER_RESOURCE
            r = s.request('GET',url)
            self.assertNotEqual(r.status_code,200)

    def testAdminAccess(self):
        with requests.Session() as s:
            url = URL + self.ADMIN_RESOURCE
            r = s.request('GET',url)
            self.assertNotEqual(r.status_code,200)


class AuthTest(AuthBaseTest):
    auth_token=('student@example.com','password')
    ANON_RESOURCE = '/anon/Principal'
    USER_RESOURCE = '/auth/Principal'
    ADMIN_RESOURCE = '/admin/Principal'

    def testAnonAccess(self):
        with requests.Session() as s:
            url = URL + self.ANON_RESOURCE
            r = s.request('GET',url,auth=self.auth_token)
            self.assertEqual(r.status_code,200)
     
    def testUserAccess(self):
        with requests.Session() as s:
            url = URL + self.USER_RESOURCE
            r = s.request('GET',url,auth=self.auth_token)
            self.assertEqual(r.status_code,200)

    def testAdminAccess(self):
        with requests.Session() as s:
            url = URL + self.ADMIN_RESOURCE
            r = s.request('GET',url,auth=self.auth_token)
            self.assertNotEqual(r.status_code,200)


class AdminTest(AuthBaseTest):
    auth_token=('admin@example.com','password')
    ANON_RESOURCE = '/anon/Principal'
    USER_RESOURCE = '/auth/Principal'
    ADMIN_RESOURCE = '/admin/Principal'

    def testAnonAccess(self):
        with requests.Session() as s:
            url = URL + self.ANON_RESOURCE
            r = s.request('GET',url,auth=self.auth_token)
            self.assertEqual(r.status_code,200)
     
    def testUserAccess(self):
        with requests.Session() as s:
            url = URL + self.USER_RESOURCE
            r = s.request('GET',url,auth=self.auth_token)
            self.assertEqual(r.status_code,200)

    def testAdminAccess(self):
        with requests.Session() as s:
            url = URL + self.ADMIN_RESOURCE
            r = s.request('GET',url,auth=self.auth_token)
            self.assertEqual(r.status_code,200)

if __name__ == '__main__':
    unittest.main()
