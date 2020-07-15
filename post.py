import requests
import json

def post():
    pload1 = {
    "params": {
        "db": "bitnami_odoo",
        "login": "user@example.com",
        "password": "user@123"
    }
}
    headers = {'Content-Type': 'application/json'}
    r1 = requests.post('http://34.87.62.211/web/session/authenticate/',headers=headers,json = pload1)
    print(r1.text)

    pload2 = {"jsonrpc": "2.0","method": "call","params": {"args": [[["user_id","=",2]],["attendance_state","name"]],"model": "hr.employee","method": "search_read","kwargs": {}},"id": 49458607}
    headers = {'Content-Type': 'application/json'}
    print(r1.cookies)
    r2 = requests.post('http://34.87.62.211/web/dataset/call_kw/hr.employee/search_read',headers=headers,json = pload2,cookies=r1.cookies)
  
    print(r2.text)

post()
