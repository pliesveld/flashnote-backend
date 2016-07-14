import unittest
import os
from .settings import URL


class EnvironmentTest(unittest.TestCase):
    "Tests the environment variables exported from PythonUnitTests.class"

    @unittest.skipIf(os.getenv('INTEGRATION_TEST_PORT') is None, "Environemnt variable not present")
    def testEnvironmentPort(self):
        port = int(os.environ['INTEGRATION_TEST_PORT'])
        self.assertTrue(port > 1 or port < 65536)

    @unittest.skipIf(os.getenv('INTEGRATION_TEST_HOST') is None, "Environemnt variable not present")
    def testEnvironmentHost(self):
        os.environ['INTEGRATION_TEST_HOST']

    @unittest.skipIf(os.getenv('INTEGRATION_TEST_SCHEME') is None, "Environemnt variable not present")
    def testEnvironmentScheme(self):
        scheme = os.environ['INTEGRATION_TEST_SCHEME'].lower()
        self.assertTrue(scheme.lower() == 'http' or scheme.lower() == 'https')


    @unittest.skipIf(os.getenv('INTEGRATION_TEST_PROFILES') is None, "Environemnt variable not present")
    def testEnvironmentAuth(self):
        profiles = os.environ['INTEGRATION_TEST_PROFILES'].lower()
        print(profiles)

