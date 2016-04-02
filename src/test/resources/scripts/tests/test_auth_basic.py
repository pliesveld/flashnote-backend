import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools

try:
    from .settings import *
except SystemError:
        from settings import *

ANON_RESOURCE =  URL + 'anon/Principal'
USER_RESOURCE =  URL + 'auth/Principal'
ADMIN_RESOURCE = URL + 'admin/Principal'

class AuthBaseTest(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        pass

    def tearDown(self):
        pass

    def getAuthToken(self):
        return None

    def invoke_request(self, resource, status_expected):
        auth = self.getAuthToken()
        req = requests.Request(method='GET', url=resource, auth=auth)
        req = req.prepare()

        log.debug("sending headers: " + str(req.headers.__dict__))

        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)

        try:
            log.debug("response: " + json.dumps(r.json(), sort_keys=True, indent=4))
        except ValueError:
            log.debug("response: " + str(r.content))

        self.assertEqual(r.status_code,status_expected)

class AnonTest(AuthBaseTest):
    auth = None

    def getAuthToken(self):
        return self.auth

    def testAnonAccess(self):
        super().invoke_request(ANON_RESOURCE,200)
     
    def testUserAccess(self):
        super().invoke_request(USER_RESOURCE,401)

    def testAdminAccess(self):
        super().invoke_request(ADMIN_RESOURCE,401)


class AuthTest(AuthBaseTest):
    auth = ('student@example.com','password')

    def getAuthToken(self):
        return self.auth

    def testAnonAccess(self):
        super().invoke_request(ANON_RESOURCE,200)
         
    def testUserAccess(self):
        super().invoke_request(USER_RESOURCE,200)

    def testAdminAccess(self):
        super().invoke_request(ADMIN_RESOURCE,403)


class AdminTest(AuthBaseTest):
    auth = ('admin@example.com','password')

    def getAuthToken(self):
        return self.auth

    def testAnonAccess(self):
        super().invoke_request(ANON_RESOURCE,200)
     
    def testUserAccess(self):
        super().invoke_request(USER_RESOURCE,200)
        
    def testAdminAccess(self):
        super().invoke_request(ADMIN_RESOURCE,200)

if __name__ == '__main__':
    unittest.main()
