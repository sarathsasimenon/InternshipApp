import requests
import json

def post():
    pload1 = {"params": {
        "db": "Deceler_test_15-july-2020",
        "login": "vinoth@inspirenetworks.in",
        "password": "123"}
    }
    headers = {'Content-Type': 'application/json'}
    r1 = requests.post('http://34.87.169.30/web/session/authenticate/',headers=headers,json = pload1)
    print(r1.text)

    pload2 = {
    "jsonrpc": "2.0",
    "method": "call",
    "params": {
        "args": [
            [
                [
                    "user_id",
                    "=",
                    6
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
    # print(r1.cookies)
    r2 = requests.post('http://34.87.169.30/web/dataset/call_kw/hr.employee/search_read',headers=headers,json = pload2,cookies=r1.cookies)
    
    dict1 = json.loads(r2.text)
    for i in dict1:
        if (i == "result"):
            lis = dict1[i]
            dict2 = lis[0]
    for j in dict2:
        if(j == "attendance_state"):
            res = dict2[j]
    
    if(res == "checked_out"):
        fres = "checked out."
    else:
        fres = "checked in."
    

post()
