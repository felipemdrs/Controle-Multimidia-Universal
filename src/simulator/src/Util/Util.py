import httplib
import urllib
import socket
from twisted.internet.error import ConnectBindError


def httpGetRequest(ip, port, metodo):
    conn = httplib.HTTPConnection(ip, port)
    conn.request("GET", "/" + metodo)
    r1 = conn.getresponse()
    return r1.read()


def httpPostRequest(ip, port, metodo, params):
    print ip, port, metodo, params
    headers = {"Content-type": "application/x-www-form-urlencoded", "Accept": "text/plain"}
    params = urllib.urlencode(params)
    conn = httplib.HTTPConnection(ip, port)
    conn.request("POST", "/" + metodo, params, headers)
    r1 = conn.getresponse()
    return r1.read()


def get_local_ip_address(target):
    ipaddr = ''
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect((target, 8000))
        ipaddr = s.getsockname()[0]
        s.close()
    except ConnectBindError:
        print("Not Ip")
    return ipaddr
