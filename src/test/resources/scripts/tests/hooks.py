import json
import logging as log

def print_url(r, *args, **kwargs):
    print(r.url)

def print_json(r, *args, **kwargs):
    r_json = r.json()
    print(json.dumps(r_json, sort_keys=True, indent=4))

def log_json(r, *args, **kwargs):
    r_json = r.json()
    log.debug(json.dumps(r_json, sort_keys=True, indent=4))

