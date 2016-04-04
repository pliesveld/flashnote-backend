import os
import os.path;
import sys
import logging as log

SCHEME=os.getenv('INTEGRATION_TEST_SCHEME',"http")
HOST=os.getenv('INTEGRATION_TEST_HOST',"localhost")
PORT=os.getenv('INTEGRATION_TEST_PORT',9000)
PROFILES=os.getenv('INTEGRATION_TEST_PROFILES','')

URL=SCHEME + "://" + HOST + ":" + str(PORT) + "/"
DATA_DIR="/home/happs/projects/flashnote/src/test/resources/scripts/tests/test-data"


if os.getenv('DEBUG'):
    DEBUG=True
else:
    DEBUG=False

log.basicConfig( stream=sys.stdout, level=log.WARN if DEBUG == False else log.DEBUG)
log.getLogger().setLevel(log.WARN if DEBUG == False else log.DEBUG)

log.debug("__file__" +  __file__)
log.debug("Spring Profiles: " + PROFILES)
log.debug("default url: " + URL)

try:
    root_dir = os.path.dirname(__file__)
    DATA_DIR = os.path.join(root_dir,'test-data')
    assert os.path.exists(DATA_DIR)
except NameError:
    log.warn("Could not determine dir based on __file__")
    assert os.path.exists(DATA_DIR)

log.debug("Using DATA_DIR: " + DATA_DIR)

def loadResource(*path):
    new_dir = os.path.join(DATA_DIR,*path)
    #print("loading ", new_dir)
    assert os.path.exists(new_dir)
    assert os.path.isfile(new_dir)
    return (new_dir, os.path.basename(new_dir))
