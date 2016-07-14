import json
import requests
from requests.exceptions import *
import unittest

from .settings import URL, loadResource, noauthtest
from .routes import ATTACHMENT_UPLOAD
from .test_base import BaseTestCase, assert_2xx, assert_not_2xx

class GoldieTest(BaseTestCase):

    def testFileUploadImage(self):
        self.url = ATTACHMENT_UPLOAD
        self.upload = ('image', 'puppy512x512.jpg')
        self.contenttype = 'image/jpeg'

        req = self.request()
        r = req(hooks=assert_2xx)

        self.assertTrue('location' in r.headers)
        url_attachment = r.headers.get('location')
        r = self.s.request('HEAD', url_attachment)
        r = self.s.request('DELETE', url_attachment)

    def testFileUploadImageTooLarge(self):

        self.url = ATTACHMENT_UPLOAD
        self.upload = ('image', 'puppy.jpg')
        self.contenttype = 'image/jpeg'

        req = self.request()
        r = req(hooks=assert_not_2xx)

    def testFileUploadImageReallyUnsupportedBMP(self):
        self.url = ATTACHMENT_UPLOAD
        self.upload = ('image', 'puppy.bmp')
        self._filename = 'puppy.jpg'
        self.contenttype = 'image/jpeg'
        req = self.request()
        r = req(hooks=assert_not_2xx)

    def testFileUploadSmallBMP(self):
        self.url = ATTACHMENT_UPLOAD
        self.upload = ('image', 'dot.bmp')
        self.contenttype = 'image/jpeg'
        req = self.request()
        r = req(hooks=assert_not_2xx)

class AudioTest(BaseTestCase):

    def testFileUploadAudioWav(self):
        self.url = ATTACHMENT_UPLOAD
        self.upload = ('audio', 'sample.wav')
        self.contenttype = 'audio/wav'
        req = self.request()
        r = req(hooks=assert_2xx)
        self.assertTrue('location' in r.headers)
        url_attachment = r.headers.get('location')
        r = self.s.request('HEAD', url_attachment)

    def testFileUploadAudioMp3(self):
        self.url = ATTACHMENT_UPLOAD
        self.upload = ('audio', 'sample.mp3')
        self.contenttype = 'audio/mp3'
        req = self.request()
        r = req(hooks=assert_2xx)
        self.assertTrue('location' in r.headers)
        url_attachment = r.headers.get('location')
        r = self.s.request('HEAD', url_attachment)

if __name__ == '__main__':
    unittest.main(verbosity=2)
