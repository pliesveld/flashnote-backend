import os
import os.path;

SCHEME=os.getenv('INTEGRATION_TEST_SCHEME',"http")
HOST=os.getenv('INTEGRATION_TEST_HOST',"localhost")
PORT=os.getenv('INTEGRATION_TEST_PORT',8080)
PROFILES=os.getenv('INTEGRATION_TEST_PROFILES','')

URL=SCHEME + "://" + HOST + ":" + str(PORT) + "/"
DATA_DIR="/home/happs/projects/flashnote/src/test/resources/scripts/tests/test-data"

#print("debug: ",__file__)
#print("debug: ",PORT)

try:
    root_dir = os.path.dirname(__file__)
    DATA_DIR = os.path.join(root_dir,'test-data')
    assert os.path.exists(DATA_DIR)
except NameError:
    print("Could not determine dir based on __file__")
    assert os.path.exists(DATA_DIR)
else:
    print("Using DATA_DIR: ", DATA_DIR)

def loadResource(*path):
    new_dir = os.path.join(DATA_DIR,*path)
    #print("loading ", new_dir)
    assert os.path.exists(new_dir)
    assert os.path.isfile(new_dir)
    return (new_dir, os.path.basename(new_dir))
