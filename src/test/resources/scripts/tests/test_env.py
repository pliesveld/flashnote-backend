import unittest
import os
from .settings import URL

class EnvironmentTest(unittest.TestCase):

    def testEnvironmentPort(self):
        os.environ['INTEGRATION_TEST_PORT']

