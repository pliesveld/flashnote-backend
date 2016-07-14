import os
import os.path
import json
import requests
from requests.exceptions import HTTPError
import logging as log
import unittest

try:
    from .settings import URL, DEBUG, DATA_DIR
except SystemError:
    from settings import URL, DEBUG, DATA_DIR

def print_url(r, *args, **kwargs):
    print('*'*80)
    req = r.request.copy()
    req.body = req.body[:500] + b'...'
    print(vars(req))
    print('*'*80)

def print_json(r, *args, **kwargs):
    try:
        r_json = r.json()
        print(json.dumps(r_json, sort_keys=True, indent=4))
    except ValueError:
        print(r.text)

def log_json(r, *args, **kwargs):
    try:
        r_json = r.json()
        log.debug(json.dumps(r_json, sort_keys=True, indent=4))
    except ValueError:
        log.debug(r.text)

def assert_200(r, *args, **kwargs):
    if hasattr(r, 'request') and hasattr(r.request, 'test'):
        r.request.test.assertEqual(r.status_code, 200)

def assert_not_2xx(r, *args, **kwargs):
    if hasattr(r, 'request') and hasattr(r.request, 'test'):
        test = r.request.test
        try:
            r.raise_for_status()
        except HTTPError:
            return
        test.fail('Expected failure status' + r.status_code)

def assert_2xx(r, *args, **kwargs):
    if hasattr(r, 'request') and hasattr(r.request, 'test'):
        test = r.request.test
        try:
            r.raise_for_status()
        except HTTPError:
            test.fail(r.text)
            raise

def _loadResource(*path):
    filepath = os.path.join(DATA_DIR, *path)
    assert os.path.exists(filepath)
    assert os.path.isfile(filepath)
    return (filepath, os.path.basename(filepath))


class BaseTestCase(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        self._resource = ''
        self._url = URL
        self._query = None
        self._method = None
        self._json = None

        # files
        self.contenttype = None
        self._filename = None
        self._data = None
        self._fileObj = None
        s = requests.Session()

        def sessionCloseWrapper():
            s.close()

        self.addCleanup(sessionCloseWrapper)
        self.s = s


    def tearDown(self):
        self._fileObj = None
        self.s = None

    @property
    def url(self):
        return self._url + self._resource

    @url.setter
    def url(self, value):
        log.debug("using resource: " + str(value))
        self._resource = value

    @property
    def query(self):
        return self._query

    @property
    def method(self):
        return 'GET' if self._method is None else self._method

    @query.setter
    def query(self, value):
        log.debug("using query params: " + str(value))
        self._query = value

    @property
    def files(self):
        if self._fileObj is None or self._filename is None:
            return None
        return {'file': (self._filename, self._fileObj, self.contenttype)}

    @property
    def json(self):
        return self._json

    @json.setter
    def json(self, value):
        self._method = 'POST'
        self._json = json

    @property
    def data(self):
        return self._data

    @property
    def upload(self):
        pass

    @upload.setter
    def upload(self, value):
        (filepath, self._filename) = _loadResource(*value)
        if self._fileObj is not None:
            self._fileObj.close()
            self._fileObj = None

        self._method = 'POST'
        fileObj = open(filepath, 'rb')
        self.contenttype = 'application/json'
        self._fileObj = fileObj
        self._data = {}

        def closeFileWrapper():
            log.debug("Closing file " + filepath)
            fileObj.close()

        self.addCleanup(closeFileWrapper)


    def request(self):
        files = self.files
        req = requests.Request(method=self.method, url=self.url,
                               files=self.files, data=self._data,
                               params=self._query, json=self._json)

        def requestWrapper(hooks=None):
            prepreq = req.prepare()
            prepreq.test = self

            response_hooks = []

            if DEBUG:
                response_hooks.extend([print_url, print_json])

            response_hooks.extend([log_json])

            extra_hooks = [] if hooks is None else hooks
            if isinstance(hooks, list):
                response_hooks.extend(extra_hooks)
            else:
                response_hooks.append(extra_hooks)

            prepreq.prepare_hooks(hooks=dict(response=response_hooks))
            log.debug("sending headers: " + str(prepreq.headers.__dict__))

            response = self.s.send(prepreq)
            return response
        return requestWrapper




