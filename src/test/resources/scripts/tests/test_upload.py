import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools
from .settings import URL

class GoldieTest(unittest.TestCase):
    RESOURCE = '/attachments'

    def testMultiPartFileUploadImage(self):
        with open('puppy.jpg','rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : ('puppy.jpg', fileObj, 'image/jpeg' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    r.raise_for_status()
                    self.assertTrue('location' in r.headers)
                    url_attachment = r.headers.get('location')
                    r = s.request('HEAD',url_attachment)
                    r.raise_for_status()
                    r = s.request('DELETE',url_attachment)
                    r.raise_for_status()

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise


    def testMultiPartFileUploadImageReallyUnsupportedBMP(self):
        with open('puppy.bmp','rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : ('puppy.jpg', fileObj, 'image/jpeg' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    try:
                        r.raise_for_status()
                    except HTTPError:
                        print(r.text)

                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise


    def testMultiPartFileUploadSmallBMP(self):
        with open('dot.bmp','rb') as fileObj:
            with requests.Session() as s:
                try:
                    url = URL + self.RESOURCE
                    files = { 'file' : ('puppy.jpg', fileObj, 'image/jpeg' ) }
                    data = {}

                    r = s.request('POST',url,data=data,files=files)
                    try:
                        r.raise_for_status()
                    except HTTPError:
                        print(r.text)


                except RequestException as re:
                    print("Connection to " + URL + " raised an exception. " + str(getattr(re,'request','')) + ' ' + str(getattr(re,'response','')))
                    raise


if __name__ == '__main__':
    unittest.main(verbosity=2)
