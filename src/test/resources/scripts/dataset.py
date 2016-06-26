#!/bin/python3

import json
import sys
import logging as log
import argparse
import re



"""
Generates json DataSet file
"""

def setup_logger(verbose=0):
    if verbose == 0:
        log_level = log.WARN
    elif verbose == 1:
        log_level = log.INFO
    else:
        log_level = log.DEBUG

    log.basicConfig(stream=sys.stdout)
    log.getLogger().setLevel(log_level)

def uniqueId():
    i = 90000
    while True:
        yield i
        i = i + 1

def validQuestionChecker():
    empty = re.compile(r"^$")
    whitespace = re.compile(r"\s+")
    comment = re.compile(r"^#.*$")


    ignore_exp = [whitespace, comment, empty]

    def checker(line):
        for exp in ignore_exp:
            ro = exp.match(line)
            if ro is not None:
                log.debug("Ignoring; Expression {}, match {}".format(exp, ro))
                return False
        return True
    return checker


ID_GEN = uniqueId()

def generateids(entities, ids):
    for entity in entities:
        i = next(ID_GEN)
        entity["@id"] = i
        ids.append(i)



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



if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("--category-name", dest="category_name", type=str,
                        action="store", default='Generated Question Banks')
    parser.add_argument("--category-description", dest="category_desc", type=str,
                        action="store", default='Contains QuestionBanks that'
                        'been generated from dataset.py')
    parser.add_argument("-v", "--verbose", nargs='?', help="increase output" +
                        "verbosity; multiple v supported",
                        action=VAction, dest="verbose", default=argparse.SUPPRESS)

    parsed_args = parser.parse_args()
    parser_dict = parsed_args.__dict__
    setup_logger(parser_dict.pop('verbose', 0))
    log.debug("Arguments:" + str(parser_dict))

    validQuestion = validQuestionChecker()

    category_name = parser_dict.pop('category_name', "Test Category")
    category_desc = parser_dict.pop('category_desc', "Generated Category")


    questions = []
    for line in sys.stdin:
        if validQuestion(line):
            questions.append({"content": line.strip()})


    category_id = 500
    bank_desc = "Test Bank"
    questions_ref = []
    questionbank = {"category": category_id, "description": bank_desc,
                    "questions": questions_ref}



    generateids(questions, questions_ref)

    dataSet = {"_class": "com.pliesveld.populator.repository.reader.DataSet",
               "questions": questions,
               "categories": [{"@id": category_id,
                              "name": category_name,
                              "description": category_desc}],
               "questionBanks": [questionbank]}


    log.debug("Read {} questions".format(len(questions)))

    print(json.dumps(dataSet, indent=True, sort_keys=True))

