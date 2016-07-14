import json
import re

try:
    from .settings import loadResource
except SystemError:
    from settings import loadResource



def createCategory(chapter, description):
    return {
            '_class' : 'com.pliesveld.flashnote.domain.Category',
            'description': description,
            'name': 'War And Peace ' + chapter
           }


FILE_CNT = 0
def fileid():
    global FILE_CNT
    FILE_CNT = FILE_CNT + 1
    while True:
        yield FILE_CNT


def writeCategory(category):
    filename_prefix = 'test-category-war-'
    filename_suffix = '.json'

    filename = filename_prefix + str(next(fileid())) + filename_suffix

    with open(filename,'w') as fileObj:
        json.dump(category, fileObj)



(filepath, filename) = loadResource('text', '2600.txt');

with open(filepath, 'r') as fileObj:
    chapter = 'Untitled'
    book = str(fileObj.read())
    offset = 0
    chunk_size = 512

    file_length = len(book)
    print(file_length)

    chapters_found = 0

    while offset < file_length:

        print("offset:", offset)
        print("chunksize:", chunk_size)

        data = book[offset:chunk_size+offset]

        offset = offset + chunk_size

        ro = re.search('^(CHAPTER [IXV]+)', data, re.MULTILINE)
        if(ro is None):
            pass
        else:
            chapter = ro.group(0)
            chapters_found = chapters_found + 1


        category = createCategory(chapter, data);
        writeCategory(category)



