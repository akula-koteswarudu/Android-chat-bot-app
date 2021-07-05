from flask import  Flask
from flask import request
import aiml

app = Flask(__name__)

kernel = aiml.Kernel()
kernel.learn("std-startup.xml")
kernel.respond('load aiml b')



@app.route('/')
def hello():
    return 'Hello, World!'

@app.route('/info',methods=['POST'])
def hello_world():
    sessionid = int(request.form['sessionid'])
    name = request.form['name']
    mname = request.form['mname']
    fname = request.form['fname']
    pno1 = request.form['pno1']
    pno2 = request.form['pno2']
    address = request.form['address']
    print(sessionid,mname,fname,pno1,pno2,address)
    kernel.setPredicate('name',name,sessionid)
    kernel.setPredicate('mname',mname,sessionid)
    kernel.setPredicate('fname',fname,sessionid)
    kernel.setPredicate('pno1',pno1,sessionid)
    kernel.setPredicate('pno2',pno2,sessionid)
    kernel.setPredicate('address',address,sessionid)
    return 'hello'


@app.route('/post',methods=['POST'])
def hello_post():
    sessionid = int(request.form['sessionid'])
    value = request.form['value']
    res = kernel.respond(value,sessionid)
    return res

if __name__=="__main__":
    app.run("0.0.0.0")