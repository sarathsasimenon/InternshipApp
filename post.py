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
    # print(r1.text)

    # pload2 = {
    # "jsonrpc": "2.0",
    # "method": "call",
    # "params": {
    #     "args": [
    #         [
    #             [
    #                 "user_id",
    #                 "=",
    #                 6
    #             ]
    #         ],
    #         [
    #             "attendance_state",
    #             "name"
    #         ]
    #     ],
    #     "model": "hr.employee",
    #     "method": "search_read",
    #     "kwargs": {}
    # },
    # "id": 49458607
    # }
    # headers = {'Content-Type': 'application/json'}
    # # print(r1.cookies)
    # r2 = requests.post('http://34.87.169.30/web/dataset/call_kw/hr.employee/search_read',headers=headers,json = pload2,cookies=r1.cookies)
    
    # dict1 = json.loads(r2.text)
    # for i in dict1:
    #     if (i == "result"):
    #         lis = dict1[i]
    #         dict2 = lis[0]
    # for j in dict2:
    #     if(j == "attendance_state"):
    #         res = dict2[j]
    
    # if(res == "checked_out"):
    #     fres = "checked out."
    # else:
    #     fres = "checked in."

    pload3 = {
                "jsonrpc": "2.0",
                "method": "call",
                "params": {
                    "model": "stock.picking",
                    "domain": [],
                    "fields": [
                        "name",
                        "location_dest_id",
                        "partner_id",
                        "date",
                        "scheduled_date",
                        "origin",
                        "group_id",
                        "backorder_id",
                        "state",
                        "priority",
                        "picking_type_id"
                    ],
                    "limit": 80,
                    "sort": "",
                    "context": {
                        "lang": "en_US",
                        "tz": "Asia/Kolkata",
                        "uid": 6,
                        "params": {
                            "action": 371,
                            "model": "stock.picking",
                            "view_type": "list",
                            "menu_id": 228
                        },
                        "contact_display": "partner_address"
                    }
                },
                "id": 908927024
            }
    headers = {'Content-Type': 'application/json'}
    r3 = requests.post('http://34.87.169.30/web/dataset/search_read',headers=headers,json = pload3,cookies=r1.cookies)

    dict2 = json.loads(r3.text)
    for i in dict2:
        if(i == 'result'):
            dict21 = dict2[i]
    for i in dict21:
        if(i == 'records'):
            lis = dict21[i]

    doc = {}
    for i in range (len(lis)):
        s = "WH/OUT"
        flag = 0
        for key in lis[i]:
            if(key=="id"):
                orderid = lis[i][key]
            if(key=="name"):
                if(s in lis[i][key]):
                    name = lis[i][key]
                    flag = 1
            if(key=="partner_id" and flag ==1):
                compname = lis[i][key][1]
            if(key=="state" and flag == 1):
                if(lis[i][key] != "done" and lis[i][key] != "cancel"):
                    state = lis[i][key]
                    l = [orderid,compname,state]
                    doc[name] = l
    req = input()
    num = []
    for i in doc:
        num.append(i)
    if(req in num):
        resp = "Order name: "+doc+", Comp name: "+doc[req][1]+", State: "+doc[req][2]
    else:
        resp = "Enter correct order id"

    # response = "Delivery Order Status is as follows:"
    # for i in doc:
    #     response = response + "\nOrder name: "+i+", State: "+doc[i].title()
    
    print(resp)
    # pload2 = {
    #         "jsonrpc":"2.0",
    #         "method":"call",
    #         "params":{"model":"sale.order",
    #         "domain":[["state","not in",["draft","sent","cancel"]]],
    #         "fields":["message_needaction","name","confirmation_date","commitment_date","expected_date","partner_id","user_id","amount_untaxed","currency_id","is_not_approval","invoice_status","state"],
    #         "limit":80,
    #         "sort":"",
    #         "context":{"lang":"en_US","tz":"Asia/Kolkata","uid":7}},
    #         "id":473430147}

    # headers = {'Content-Type': 'application/json'}
            
    # r2 = requests.post('http://34.87.169.30/web/dataset/search_read',headers=headers,json = pload2,cookies=r1.cookies)
    
    # dict1 = json.loads(r2.text)
    # for item in dict1["result"]["records"]:
    #     print(item)
    #     # res = item["date"]  
    #     # #res1= item["partner_id"][1]  
    #     # res2= item["state"]  
    #     # res3=item["origin"]  

    
def post2():
    r3 = requests.get('https://qq7q2qo5y1dlxxc-vinothocidb.adb.ap-mumbai-1.oraclecloudapps.com/ords/xxtest/sarath/')
    print(r3.text)
post2()
