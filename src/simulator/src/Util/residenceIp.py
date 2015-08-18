import Util
from httplib import BadStatusLine

from socket import socket, AF_INET, SOCK_STREAM


def isResidence(addr):
    try:
        s = socket(AF_INET, SOCK_STREAM)
        s.settimeout(0.01)
        print addr
        if ((not s.connect_ex((addr, 5432))) and (Util.httpGetRequest(addr, 5432, "index") == "Welcome to Control multimedia Universal!")):
            s.close()
            return True
        else:
            s.close()
            return False
    except BadStatusLine:
        print "Endereco invalido"


def getIp():
    addr = ""
    if (isResidence("127.0.0.1")):
        return "127.0.0.1"
    for ip1 in xrange(3):
        for ip2 in xrange(0, 256):
            addr = "192.168." + str(ip1) + "." + str(ip2)
            if isResidence(addr):
                return addr
    return "Residence is offline"
