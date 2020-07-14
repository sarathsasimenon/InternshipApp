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

def post2():
    pload = {
        "jsonrpc": "2.0",
        "method": "call",
        "params": {
            "args": [
                [
                    [
                        "user_id",
                        "=",
                        2
                    ]
                ],
                [
                    "attendance_state",
                    "name"
                ]
            ],
            "model": "hr.employee",
            "method": "search_read",
            "kwargs": {}
        },
        "id": 49458607
    }
    headers = {'Content-Type': 'application/json'}
    r = requests.post('http://35.240.205.55/web/dataset/call_kw/hr.employee/search_read',headers=headers,data = json.dumps(pload))
    return(r.text)

print(post())
# print(post2())