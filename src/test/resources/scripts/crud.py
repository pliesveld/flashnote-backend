import os
import os.path;
import sys
import logging as log

import argparse
import functools


import json
import requests
from requests.exceptions import *
from requests.adapters import HTTPAdapter
import json.decoder

SCHEME=os.getenv('INTEGRATION_TEST_SCHEME',"http")
HOST=os.getenv('INTEGRATION_TEST_HOST',"localhost")
PORT=os.getenv('INTEGRATION_TEST_PORT',9000)
PROFILES=os.getenv('INTEGRATION_TEST_PROFILES','')

URL=SCHEME + "://" + HOST + ":" + str(PORT) + "/"
DATA_DIR="/home/happs/projects/flashnote/src/test/resources/scripts/tests/test-data"


s = requests.Session()
s.mount('http://', HTTPAdapter(max_retries=1))

class Crud(object):

    def __init__(self, resource, verbose=False, user=None, password=None, login=False, payload=None, **kwargs):
        log.basicConfig( stream=sys.stdout, level=log.WARN if verbose == False else log.DEBUG)
        log.getLogger().setLevel(log.WARN if verbose == False else log.DEBUG)

        self.auth = { 'username' : user,
                      'password' : password }

        self.token = None

        if login:
            self.token = self.login()

        request_params = { 'url' : URL + resource,
                           'method' : 'GET' }

        if payload:
            request_params.update({ 'method' : 'POST', 'json' : payload})

        log.debug("request params : " + str(request_params));

        req = requests.Request(**request_params)
        req.headers['X-AUTH-TOKEN'] = self.token
        req = req.prepare()

        log.debug("headers: " + str(req.headers.__dict__))
        log.info("sending : " + str(req.__dict__))

        r = requests.models.Response()
        with requests.Session() as s:
            r = s.send(req)

            if len(r.text) == 0:
                log.info("no content: status " + str(r.status_code))
            else:
            
                try:
                    r_json = r.json()
                    print(json.dumps(r_json, sort_keys=True, indent=4))
                    log.info("response: " + json.dumps(r_json, sort_keys=True, indent=4))
                except ValueError:
                    print(r.text)
                    log.warn("response: " + r.text)



 


    def login(self):
        USER_RESOURCE =  URL + 'user'
        AUTH_RESOURCE =  URL + 'auth'

        req = requests.Request(method='POST', url=AUTH_RESOURCE,json=self.auth)
        req = req.prepare()

        log.debug("sending headers: " + str(req.headers.__dict__))
        log.debug("sending : " + str(req.__dict__))
        r = requests.models.Response()
        token = None

        with requests.Session() as s:
            r = s.send(req)
            r_json = r.json()

            token = r_json['token']

            req = requests.Request(method='GET', url=USER_RESOURCE)
            req.headers['X-AUTH-TOKEN'] = token
            req = req.prepare()

            log.debug("sending headers: " + str(req.headers.__dict__))
            r = requests.models.Response()

            r = s.send(req)

            r_json = r.json()
            log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))
            self.token = token
        return token

parser = argparse.ArgumentParser()
parser.add_argument("-u", "--user", help="Authentication Credentials", default='student@example.com')
parser.add_argument("-p", "--password", help="Authentication Credentials", default='password')

parser.add_argument("--host", help="Override host", default=argparse.SUPPRESS)
parser.add_argument("--port", help="Override port", default=argparse.SUPPRESS)

parser.add_argument("--login", action="store_const", const=False, default=True)

parser.add_argument("resource", type=str, action="store")

parser.add_argument("--json", dest="payload", type=json.loads, action="store", default=argparse.SUPPRESS)

parser.add_argument("-v", "--verbose", help="increase output verbosity",
                    action="store_true")
args = parser.parse_args()

print(args)

crud = Crud(**args.__dict__)

