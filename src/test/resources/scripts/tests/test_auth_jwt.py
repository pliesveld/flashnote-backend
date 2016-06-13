import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools
import os

try:
    from .settings import *
    from .hooks import *
except SystemError:
        from settings import *
        from hooks import *

USER_RESOURCE =  URL + 'user'
AUTH_RESOURCE =  URL + 'auth'
REFRESH_RESOURCE =  URL + 'refresh'

class TokenTest(unittest.TestCase):

    def testTokenHeader(self):
        auth_req = { 'username' : 'student@example.com',
                     'password' : 'password' }

        req = requests.Request(method='POST', url=AUTH_RESOURCE,json=auth_req)
        req = req.prepare()

        log.debug("sending headers: " + str(req.headers.__dict__))
        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)

        r_json = r.json()

        log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))

        self.assertIn('token', r_json)


    def testUserByToken(self):
        auth_req = { 'username' : 'student@example.com',
                     'password' : 'password' }

        req = requests.Request(method='POST', url=AUTH_RESOURCE,json=auth_req)

        req = req.prepare()
#        req.prepare_hooks(hooks=dict(response=[print_json,log_json]))

        log.debug("sending headers: " + str(req.headers.__dict__))
        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)
            r_json = r.json()

            self.assertIn('token', r_json)
            token = r_json['token']

            req = requests.Request(method='GET', url=USER_RESOURCE)
            req.headers['X-AUTH-TOKEN'] = token
            req = req.prepare()

            log.debug("sending headers: " + str(req.headers.__dict__))
            r = requests.models.Response()

            r = s.send(req)

            r_json = r.json()
            log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))

    def testTokenRefresh(self):

        auth_req = { 'username' : 'student@example.com',
                     'password' : 'password' }

        req = requests.Request(method='POST', url=AUTH_RESOURCE,json=auth_req)
        req = req.prepare()

        log.debug("sending headers: " + str(req.headers.__dict__))
        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)
            r_json = r.json()

            self.assertIn('token', r_json)
            token = r_json['token']

            req = requests.Request(method='GET', url=REFRESH_RESOURCE)
            req.headers['X-AUTH-TOKEN'] = token
            req = req.prepare()

            log.debug("sending headers: " + str(req.headers.__dict__))
            r = requests.models.Response()

            r = s.send(req)

            r_json = r.json()
            log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))


    def testTokenBad(self):
        auth_req = { 'username' : 'student@example.com',
                     'password' : 'nottherightpassword' }

        req = requests.Request(method='POST', url=AUTH_RESOURCE,json=auth_req)
        req = req.prepare()

        log.debug("sending headers: " + str(req.headers.__dict__))
        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)

        r_json = r.json()

        log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))

        self.assertNotIn('token', r_json)

#    def testToken(self):
#        token = os.getenv('TOKEN', 'INVALID')
##
#
#        self.assertNotEqual(token, 'INVALID')
##
#        with requests.Session() as s:
##
#            req = requests.Request(method='GET', url=USER_RESOURCE)
#            req.headers['X-AUTH-TOKEN'] = token
#            req = req.prepare()
##
#            log.debug("sending headers: " + str(req.headers.__dict__))
#            r = requests.models.Response()
##
#            r = s.send(req)
##
#            r_json = r.json()
#            log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))
##
#            self.assertEqual(r.status_code, 200)
#            
##

if __name__ == '__main__':
    unittest.main()
