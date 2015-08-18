# encoding: utf-8
import cherrypy
from Residencia import *  # @UnusedWildImport
import threading as t  # @UnusedWildImport


class ServerPrincipal:

    def __init__(self, residence):
        self.residence = residence

    def index(self):
        return "Welcome to Control multimedia Universal!"
    index.exposed = True

    def default(self):  # isso eh tipo uma excecao
        return "Invalid Data"
    default.exposed = True  # deixa o metodo visivel no server

    @cherrypy.tools.allow(methods=['POST'])
    def addRoom(self, nameRoom, ip, port):
        if(self.residence.addRoom(nameRoom, ip, port)):
            return "Comodo adicionado"
        else:
            return "Comodo já existe"
    addRoom.exposed = True

    @cherrypy.tools.allow(methods=['POST'])
    def removeRoom(self, nameRoom):
        if (self.residence.removeRoom(nameRoom)):
            return "Comodo removido"
        else:
            return "Comodo nao existe"
    removeRoom.exposed = True

    @cherrypy.tools.allow(methods=['POST'])
    def sendCommand(self, nameRoom, equipment, command):
        return self.residence.sendCommand(nameRoom, equipment, command)
    sendCommand.exposed = True

    @cherrypy.tools.allow(methods=['POST'])
    def setRoomOfControl(self, nameRoom):
        return self.residence.setRoomOfControl(nameRoom)
    setRoomOfControl.exposed = True

    def getRooms(self):
        return "|".join(self.residence.getRooms())
    getRooms.exposed = True

    def powerOffEquipmentsRoom(self):
        return self.residence.powerOffEquipmentsRoom()
    powerOffEquipmentsRoom.exposed = True

residence = Residencia()


class MyThread(t.Thread):
    def run(self):
        residence.powerOffEquipmentsRoom()

mt = MyThread()
mt.start()

cherrypy.config.update("config.cfg")
cherrypy.quickstart(ServerPrincipal(residence))
