import json
import requests
from requests.exceptions import *
import unittest
import sys
import functools
import os


try:
    from .settings import *
    from .test_base import BaseTestCase, assert_2xx
    from .routes import BANK_RESOURCE, ATTACHMENT_UPLOAD
except SystemError:
    from test_base import BaseTestCase, assert_2xx
    from settings import *
    from routes import BANK_RESOURCE, ATTACHMENT_UPLOAD

class QuestionBankCreateTest(BaseTestCase):

    category = {'category': {'id': 500}}
    bank = {'description': 'a test question bank'}

    def setUp(self):
        super().setUp()
        self.bank.update(self.category)

    def testJSONSerialize(self):
        string = json.dumps(self.bank)
        print(string)

    def testCreateQuestionBankCreate(self):
        self.url = BANK_RESOURCE
        self.json = self.bank
        req = self.request()
        r = req(hooks=assert_2xx)

        bank = r.json()
        print(bank)


class QuestionBankUpdateCategoryAndDescriptionTest(BaseTestCase):

    category = {'category': {'id': 500}}
    bank = {'description': 'a test question bank'}

    def setUp(self):
        super().setUp()
        self.bank.update(self.category)
        self.url = BANK_RESOURCE
        self.json = self.bank
        req = self.request()
        r = req(hooks=assert_2xx)
        self.bank2 = r.json()
        self.assertIn('id', self.bank2)
        self.bankId = self.bank2.get('id')

    def testUpdateQuestionBankDescription(self):
        self.url = BANK_RESOURCE
        self.bank2.update({'description': 'NEW DESCRIPTION'})
        self.json = self.bank2
        self.method = 'PUT'
        req = self.request()
        r = req(hooks=assert_2xx)
        bank = r.json()

        self.assertEqual(self.bankId, bank.get('id'))

    def testUpdateQuestionBankCategory(self):

        category_new = {'category': {'id': 501}}
        self.url = BANK_RESOURCE
        print('='*90)
        print(self.bank2)
        self.bank2.update(category_new)
        self.json = self.bank2
        self.method = 'PUT'
        req = self.request()
        r = req(hooks=assert_2xx)
        bank = r.json()
        print(bank)

        self.assertEqual(self.bankId, bank.get('id'))

class QuestionBankSharedQuestionTest(BaseTestCase):

    category = {'category': {'id': 500}}
    bank = {'description': 'Adding question to existing questionbank'}
    question = {'content': 'A blank question.'}

    def setUp(self):
        super().setUp()
        self.bank.update(self.category)
        self.bank.update({'questions': [self.question]})
        self.url = BANK_RESOURCE
        self.json = self.bank
        req = self.request()
        r = req(hooks=assert_2xx)
        self.bank2 = r.json()
        self.assertIn('id', self.bank2)
        self.bankId = self.bank2.get('id')

    def testUpdateQuestions(self):
        self.assertIn('questions', self.bank2)
        questions = self.bank2.get('questions')
        questions.append({'content': 'A new question'})
        self.json = self.bank2
        req = self.request()
        r = req(hooks=assert_2xx)

    def testCreateBankWithSharedQuestion(self):
        self.assertIn('questions', self.bank2)
        questions = self.bank2.get('questions')
        self.assertEqual(1, len(questions))
        questionId = questions[0]['id']

        bank_shared = {'description': 'A bank with a shared question',
                'questions': [{'id': questionId}]}
        bank_shared.update(self.category)
        self.json = bank_shared
        req = self.request()
        r = req(hooks=assert_2xx)
        r_json = r.json()
        sharedBankId = r_json['id']

        print(self.s.request('GET', URL + BANK_RESOURCE + '/' +
            str(self.bankId)).json())

        print(self.s.request('GET', URL + BANK_RESOURCE + '/' +
            str(sharedBankId)).json())


class QuestionBankUpdateTest(BaseTestCase):

    category = {'category': {'id': 500}}
    bank = {'description': 'Adding question to existing questionbank'}
    question = {'content': 'A test question bank.'}

    def setUp(self):
        super().setUp()
        self.bank.update(self.category)
        self.bank.update({'questions': [self.question]})
        self.url = BANK_RESOURCE
        self.json = self.bank
        req = self.request()
        r = req(hooks=assert_2xx)
        self.bank2 = r.json()
        self.assertIn('id', self.bank2)
        self.bankId = self.bank2.get('id')
        questions = self.bank2.get('questions')
        self.assertEqual(1, len(questions))
        self.questionId = questions[0]['id']


    def testUpdateQuestion(self):
        self.assertIn('questions', self.bank2)
        questions = self.bank2.get('questions')
        questions.append({'content': 'A new question with an image attachment'})
        self.json = self.bank2
        req = self.request()
        r = req(hooks=assert_2xx)
        bank = r.json()
        bankId = bank.get('id')
        questions = bank.get('questions')
        questionId = questions[0]['id']

        self.url = ATTACHMENT_UPLOAD
        self.query = {'questionId': self.questionId}
        self.upload = ('image', 'puppy512x512.jpg')
        self.contenttype = 'image/jpeg'

        req = self.request()
        r = req(hooks=assert_2xx)

class QuestionBankListTest(BaseTestCase):

    def testQuestionBankList(self):

        self.url = BANK_RESOURCE
        req = self.request()
        r = req(hooks=assert_2xx)



