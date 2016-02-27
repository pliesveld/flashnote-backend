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

class AnonTest(unittest.TestCase):
    auth_token=('student@example.com','password')
    ANON_RESOURCE = '/test/byAuth1'
    USER_RESOURCE = '/user/byAuth1'
    ADMIN_RESOURCE = '/admin/byAuth1'

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


class AuthTest(unittest.TestCase):
    auth_token=('student@example.com','password')
    ANON_RESOURCE = '/test/byAuth1'
    USER_RESOURCE = '/user/byAuth1'
    ADMIN_RESOURCE = '/admin/byAuth1'

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


class AdminTest(unittest.TestCase):
    auth_token=('admin@example.com','password')
    ANON_RESOURCE = '/test/byAuth1'
    USER_RESOURCE = '/user/byAuth1'
    ADMIN_RESOURCE = '/admin/byAuth1'

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
