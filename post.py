import requests
import json

def post():
    pload = {'params': {
        'db': 'bitnami_odoo',
        'login': 'user@exmple.com',
        'password': 'user@123'}  
        }
    headers = {'Content-Type': 'application/json'}
    r = requests.post('http://35.240.205.55/web/session/authenticate/',headers=headers,data = json.dumps(pload))
    return(r.text)

# print(post())