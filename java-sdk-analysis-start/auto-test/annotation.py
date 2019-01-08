import requests

point = "8080"

base_url = {'dev': 'http://localhost:' + point}

headers = {'terminal': '3'}

get = requests.get(base_url['dev'] + "/test01", headers=headers)

jsonResult = get.json()

if jsonResult['propertiesMap']['platform'] == "PC":
    print("OK")
else:
    print ("false")
