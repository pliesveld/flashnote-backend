import unittest
import os
from .settings import URL

class EnvironmentTest(unittest.TestCase):
    "Tests the environment variables exported from PythonUnitTests.class"

    def testEnvironmentPort(self):
        port = int(os.environ['INTEGRATION_TEST_PORT'])
        self.assertTrue(port > 1 or port < 65536)

    def testEnvironmentHost(self):
        os.environ['INTEGRATION_TEST_HOST']

    def testEnvironmentScheme(self):
        scheme = os.environ['INTEGRATION_TEST_SCHEME'].lower()
        self.assertTrue(scheme.lower() == 'http' or scheme.lower() == 'https')

    def testEnvironmentScheme(self):
        auth = os.environ['INTEGRATION_TEST_AUTH'].lower()
        self.assertTrue(auth == 'no-auth' or auth == 'basic' or auth == 'digest' or auth == 'oauth')

