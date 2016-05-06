import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools
import os

try:
    from .settings import *
except SystemError:
        from settings import *

CAT_RESOURCE =  URL + 'categories'
CAT_SEARCH_RESOURCE =  URL + 'categories/search'

class CategoryTest(unittest.TestCase):


    def testCatoriesList(self):
        query = { 'query' : 'Software' }

        req = requests.Request(method='GET', url=CAT_RESOURCE)
        req = req.prepare()
        
        log.debug("sending headers: " + str(req.headers.__dict__))
        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)

        r_json = r.json()

        log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))

        self.assertEqual(r.status_code, 200);

    def testCatoriesQuery(self):
        query = { 'query' : 'Software' }

        req = requests.Request(method='GET', url=CAT_SEARCH_RESOURCE, params=query)
        req = req.prepare()
        
        log.debug("sending headers: " + str(req.headers.__dict__))
        r = requests.models.Response()

        with requests.Session() as s:
            r = s.send(req)

        r_json = r.json()

        log.debug("response: " + json.dumps(r_json, sort_keys=True, indent=4))

        self.assertEqual(r.status_code, 200);



if __name__ == '__main__':
    unittest.main()
