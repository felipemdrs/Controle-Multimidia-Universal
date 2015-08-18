from serverComodo import *  # @UnusedWildImport
from Comodo import *  # @UnusedWildImport
from ComodoUI import *  # @UnusedWildImport
import cherrypy  # @UnusedWildImport
import threading as t  # @UnusedWildImport
import sys  # @UnusedWildImport

try:
    nomeDoComodo = sys.argv[1]
except ValueError:
    print "Parametro nome ausente"
    sys.exit()

comodo = Comodo(nomeDoComodo)

server = ServerComodo(comodo)

comodoUi = ComodoUI(server, comodo)


class MyThread(t.Thread):
    def run(self):
        cherrypy.config.update("config.cfg")
        cherrypy.quickstart(server)


class Server(t.Thread):
    def run(self):
        print "Th server"
        server.Connect()

thServer = Server()
thServer.start()

mt = MyThread()
mt.start()

comodoUi.run()

