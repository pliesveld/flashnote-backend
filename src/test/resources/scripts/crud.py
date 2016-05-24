import os
import os.path
import sys
import logging as log

import argparse
import functools


import json
import requests
from requests.exceptions import *
from requests.adapters import HTTPAdapter
import json.decoder

import http.client

SCHEME=os.getenv('INTEGRATION_TEST_SCHEME',"http")
HOST=os.getenv('INTEGRATION_TEST_HOST',"localhost")
PORT=os.getenv('INTEGRATION_TEST_PORT',9000)
PROFILES=os.getenv('INTEGRATION_TEST_PROFILES','')
TOKEN=os.getenv('TOKEN',None)

URL=SCHEME + "://" + HOST + ":" + str(PORT) + "/"
DATA_DIR="/home/happs/projects/flashnote/src/test/resources/scripts/tests/test-data"


s = requests.Session()
s.mount('http://', HTTPAdapter(max_retries=1))

class Crud(object):
    sort_keys = False

    def __init__(self, resource, verbose=0, user=None, password=None, login=False, payload=None, query_params=None, **kwargs):

        if verbose == 0:
            log_level = log.WARN
        elif verbose == 1:
            log_level = log.INFO
        else:
            log_level = log.DEBUG

        #log.basicConfig( stream=sys.stdout, level=log_level)
        log.basicConfig( stream=sys.stdout )
        log.getLogger().setLevel(log_level)
        
        self.token = None

        if verbose > 2:
            http.client.HTTPConnection.debuglevel = 2 if verbose > 3 else 1
            requests_log = log.getLogger("requests.packages.urllib3")
            requests_log.setLevel(log.DEBUG)
            requests_log.propagate = True

        self.auth = { 'username' : user,
                      'password' : password }

        if TOKEN:
            log.debug("Using token from environment variable: "+ TOKEN);
            self.token = TOKEN
        else:
            if login:
                self.token = self.login(True)

        request_params = { 'url' : URL + resource,
                           'method' : 'GET',
                           'params' : query_params
                         }

        if payload:
            request_params.update({ 'method' : 'POST', 'json' : payload})

        req = requests.Request(**request_params)
        if self.token:
            req.headers['X-AUTH-TOKEN'] = self.token
        req = req.prepare()

        r = requests.models.Response()
        with requests.Session() as s:
            r = s.send(req)

            if len(r.text) != 0:
                try:
                    r_json = r.json()
                    log.info("response: " + json.dumps(r_json, sort_keys=self.sort_keys, indent=4))
                except ValueError:
                    log.info("response: " + r.text)



 


    def login(self, save_token=False):
        USER_RESOURCE =  URL + 'user'
        AUTH_RESOURCE =  URL + 'auth'
        START_RESOURCE =  URL + 'debug/startup'

        req = requests.Request(method='POST', url=AUTH_RESOURCE,json=self.auth)
        req = req.prepare()

        r = requests.models.Response()
        token = None

        with requests.Session() as s:
            r = s.send(req)
            r_json = r.json()

            if 'token' not in r_json:
                return None

            token = r_json['token']

            req = requests.Request(method='GET', url=USER_RESOURCE)
            req.headers['X-AUTH-TOKEN'] = token
            req = req.prepare()

#            log.debug("sending headers: " + str(req.headers.__dict__))
            r = requests.models.Response()

            r = s.send(req)

            r_json = r.json()
#            log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))
            self.token = token


            req = requests.Request(method='GET', url=START_RESOURCE)
            req = req.prepare()

            r = requests.models.Response()
            r = s.send(req)
            r_json = r.json()
#            log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))

            token_dict = {
                'token' : token,
                'server_start' : r.text,
            }
            
            if save_token:
                with open('token.json', 'w') as tokenfile:
                    json.dump(token_dict, tokenfile)
        return token


class VAction(argparse.Action):
    """
    argparse Action to support multiple -vvvv
    """
    def __call__(self, parser, args, values, option_string=None):
        # print 'values: {v!r}'.format(v=values)
        if values==None:
            values='1'
        try:
            values=int(values)
        except ValueError:
            values=values.count('v')+1
        setattr(args, self.dest, values)

parser = argparse.ArgumentParser()
parser.add_argument("-u", "--user", help="Authentication Credentials", default='student@example.com')
parser.add_argument("-p", "--password", help="Authentication Credentials", default='password')

parser.add_argument("--host", help="Override host", default=argparse.SUPPRESS)
parser.add_argument("--port", help="Override port", default=argparse.SUPPRESS)

parser.add_argument("--login", action="store_const", const=True, default=False)
parser.add_argument("--admin", action="store_const", dest="user", const='admin@example.com', default=argparse.SUPPRESS)

parser.add_argument("resource", type=str, action="store")

parser.add_argument("--params", help="Parameters to append to URL request", dest="query_params", type=json.loads, action="store", default=argparse.SUPPRESS)

parser.add_argument("--json", help="Payload to send with message body.  Changes method to POST", dest="payload", type=json.loads, action="store", default=argparse.SUPPRESS)

parser.add_argument("-v", "--verbose", nargs='?', help="increase output verbosity; multiple v supported",
                    action=VAction, dest="verbose", default=argparse.SUPPRESS)
args = parser.parse_args()

print(args)

crud = Crud(**args.__dict__)

