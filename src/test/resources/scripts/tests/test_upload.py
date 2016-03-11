import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools

from .settings import URL
from .settings import loadResource

class GoldieTest(unittest.TestCase):
    RESOURCE = '/attachments'

    def raise_for_status(self,r):
        "Wrapper around requests.exceptions to print response from server on error"
        try:
            r.raise_for_status()
        except HTTPError:
            self.fail(r.text)
            raise



    def testMultiPartFileUploadImage(self):
        (filepath, filename) = loadResource('image','puppy.jpg')

        with open(filepath,'rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : (filename, fileObj, 'image/jpeg' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    self.raise_for_status(r)
                    self.assertTrue('location' in r.headers)
                    url_attachment = r.headers.get('location')
                    r = s.request('HEAD',url_attachment)
                    self.raise_for_status(r)
                    r = s.request('DELETE',url_attachment)
                    self.raise_for_status(r)

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise


    def testMultiPartFileUploadImageReallyUnsupportedBMP(self):
        (filepath, filename) = loadResource('image','puppy.bmp')

        with open(filepath,'rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : (filename, fileObj, 'image/jpeg' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    self.assertNotEqual(r.status_code,200)
                    self.assertNotEqual(r.status_code,201)

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise


    def testMultiPartFileUploadSmallBMP(self):
        (filepath, filename) = loadResource('image','dot.bmp')

        with open(filepath,'rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : (filename, fileObj, 'image/jpeg' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    self.assertNotEqual(r.status_code,200)
                    self.assertNotEqual(r.status_code,201)

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise



    def testMultiPartFileUploadAudioWav(self):
        (filepath, filename) = loadResource('audio','sample.wav')

        with open(filepath,'rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : (filename, fileObj, 'audio/wav' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    self.raise_for_status(r)
                    self.assertTrue('location' in r.headers)
                    url_attachment = r.headers.get('location')
                    r = s.request('HEAD',url_attachment)
                    self.raise_for_status(r)
                    r = s.request('DELETE',url_attachment)
                    self.raise_for_status(r)

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise

    def testMultiPartFileUploadAudioMp3(self):
        (filepath, filename) = loadResource('audio','sample.mp3')

        with open(filepath,'rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    #TODO : change content-type
                    files = { 'file' : (filename, fileObj, 'audio/wav' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    self.raise_for_status(r)
                    self.assertTrue('location' in r.headers)
                    url_attachment = r.headers.get('location')
                    r = s.request('HEAD',url_attachment)
                    self.raise_for_status(r)
                    r = s.request('DELETE',url_attachment)
                    self.raise_for_status(r)

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise


if __name__ == '__main__':
    unittest.main(verbosity=2)
