"""
Utility for testing FlashNote webservice
"""

import os
import os.path
import sys
import logging as log

import argparse


import json
import requests
from requests.adapters import HTTPAdapter
import json.decoder

import http.client

SCHEME = os.getenv('INTEGRATION_TEST_SCHEME', "http")
HOST = os.getenv('INTEGRATION_TEST_HOST', "localhost")
PORT = os.getenv('INTEGRATION_TEST_PORT', 9000)
PROFILES = os.getenv('INTEGRATION_TEST_PROFILES', '')
TOKEN = os.getenv('TOKEN', None)

URL = SCHEME + "://" + HOST + ":" + str(PORT) + "/"
DATA_DIR = "/home/happs/projects/flashnote/src/test/resources/scripts/tests/test-data"


session = requests.Session()
session.mount('http://', HTTPAdapter(max_retries=1))

def log_json(r, *args, **kwargs):
    if len(r.text) != 0:
        try:
            r_json = r.json()
            log.info(json.dumps(r_json, sort_keys=True, indent=4))
        except ValueError:
            log.info(r.text)

def print_json(r, *args, **kwargs):
    if 'Location' in r.headers:
        print('Location ' + r.headers['Location'])

    if len(r.text) != 0:
        try:
            r_json = r.json()
            print(json.dumps(r_json, sort_keys=True, indent=4))
        except ValueError:
            print(r.text)


class Crud(object):
    sort_keys = False

    def __init__(self, resource, verbose=0, user=None, password=None, login=False, payload=None, query_params=None, **kwargs):

        self.token = None
        self.verbose = verbose

        self.auth = {'username': user,
                      'password': password}

        if TOKEN:
            log.debug("Using token from environment variable: " + TOKEN)
            self.token = TOKEN
        elif kwargs.get('token', False):
            self._load_token()
        else:
            if login:
                self.token = self.login(True)

        request_params = {'url': URL + resource,
                           'method': 'GET',
                           'params': query_params}

        if self.token is not None:
            request_params.update({'headers': {'X-AUTH-TOKEN':self.token}})

        if payload:
            request_params.update({'method': 'POST', 'json': payload})

        if 'verb' in kwargs:
            request_params.update({'method': kwargs.get('verb')})

        req = requests.Request(**request_params)

        req = req.prepare()
        response_callbacks = [log_json]

        if kwargs.get('print', False):
            response_callbacks.append(print_json)

        req.prepare_hooks(hooks=dict(response=response_callbacks))

        with requests.Session() as s:
            s.send(req)

    def login(self, save_token=False):
        AUTH_RESOURCE = URL + 'auth'

        req = requests.Request(method='POST', url=AUTH_RESOURCE, json=self.auth)
        req = req.prepare()

        r = requests.models.Response()
        token = None

        with requests.Session() as s:
            r = s.send(req)
            r_json = r.json()

            if 'token' not in r_json:
                return None

            token = r_json['token']

            if save_token:
                with open('token.json', 'w') as tokenfile:
                    json.dump({'token' : token}, tokenfile)

            return token

        return None


    def credentials(self):
        USER_RESOURCE = URL + 'user'
        r = requests.get(method='GET', url=USER_RESOURCE, headers={'X-AUTH-TOKEN':self.token})

        r_json = r.json()
        log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))


    def _load_token(self):
        with open('token.json', 'r') as tokenfile:
            token_dict = json.load(tokenfile)
            if 'token' in token_dict:
                self.token = token_dict['token']
        return None





class VAction(argparse.Action):
    """
    argparse Action to support multiple -vvvv
    """
    def __call__(self, pparser, args, values, option_string=None):
        # print 'values: {v!r}'.format(v=values)
        if values == None:
            values = '1'
        try:
            values = int(values)
        except ValueError:
            values = values.count('v')+1
        setattr(args, self.dest, values)



def setup_logger(verbose=0):
    if verbose == 0:
        log_level = log.WARN
    elif verbose == 1:
        log_level = log.INFO
    else:
        log_level = log.DEBUG

    log.basicConfig(stream=sys.stdout)
    log.getLogger().setLevel(log_level)


    if verbose > 2:
        http.client.HTTPConnection.debuglevel = 2 if verbose > 3 else 1
        requests_log = log.getLogger("requests.packages.urllib3")
        requests_log.setLevel(log.DEBUG)
        requests_log.propagate = True


parser = argparse.ArgumentParser()
parser.add_argument("-u", "--user", help="Authentication Credentials", default='student@example.com')
parser.add_argument("-p", "--password", help="Authentication Credentials", default='password')

parser.add_argument("--host", help="Override host", default=argparse.SUPPRESS)
parser.add_argument("--port", help="Override port", default=argparse.SUPPRESS)
parser.add_argument("--print", help="Print response", action="store_const", const=True, default=False)
parser.add_argument("--token", help="Use JWT in token.json", action="store_const", const=True, default=False)

parser.add_argument("--login", action="store_const", const=True, default=False)
parser.add_argument("--admin", action="store_const", dest="user", const='admin@example.com', default=argparse.SUPPRESS)

parser.add_argument("resource", type=str, action="store")

parser.add_argument("--params", help="Parameters to append to URL request", dest="query_params", type=json.loads, action="store", default=argparse.SUPPRESS)

parser.add_argument("--json", help="Payload to send with message body.  Changes method to POST", dest="payload", type=json.loads, action="store", default=argparse.SUPPRESS)

parser.add_argument("-v", "--verbose", nargs='?', help="increase output verbosity; multiple v supported",
                    action=VAction, dest="verbose", default=argparse.SUPPRESS)

ARGS = None

def parse_args():
    parser_args = parser.parse_args(ARGS)
    setup_logger(parser_args.__dict__.get('verbose', 0))
    return parser_args.__dict__


def do():
    parser_dict = parser.parse_args(ARGS)
    setup_logger(parser_dict.pop('verbose', 0))
    log.debug("parser_args:" + str(parser_dict))
    return Crud(**parser_dict)


if __name__ == '__main__':
    parser_dict = parse_args()
    crud = Crud(**parser_dict)
else:
    print("__name__:", __name__)




