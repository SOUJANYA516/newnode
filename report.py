import json  
import sys
import pandas as pd  
from pandas import json_normalize  

file_name = sys.argv[1]

with open('clair.json') as f: 
    d = json.load(f)
def format():
    rows = ["featurename","featureversion","vulnerability","namespace","description","link","severity","fixedby"] 
    unapprove = json_normalize(d,'unapproved') 
    row = len(unapprove)
    images = d['image']
    nycphil = json_normalize(d['vulnerabilities']) 
    unapprove = d['unapproved']
    count = len(unapprove)
    f = open(file_name,'w')
    html = """<html><table border="3" align="center">
    <br/><tr><th>Images</th><th>unapproved</th></tr>"""
    for i in range(1):
        html += "<tr><td>{}</td>".format(images)
        html += "<td>"
        for j in range(count):
            html += "{}<br />".format(unapprove[j])
        html += "</td>"
    html += "</tr>" 
    html += "</table></html>"
    f.write(html)
    f.close()    
              
    f = open(file_name,'a+')
    html = """<html><table border="3" align="center">
    <br/><tr><th>featurename</th><th>featureversion</th><th>vulnerability</th><th>namespace</th><th>description</th><th>link</th><th>severity</th><th>fixedby</th></tr>"""
    for i in range(row):
        html += "<tr><td>{}</td>".format(nycphil.iloc[i]["featurename"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["featureversion"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["vulnerability"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["namespace"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["description"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["link"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["severity"])
        html += "<td>{}</td>".format(nycphil.iloc[i]["fixedby"])          
    html += "</tr>" 
    html += "</table></html>"
    f.write(html)
    f.close() 

format() 