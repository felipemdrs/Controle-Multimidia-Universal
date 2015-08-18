# encoding: utf-8
import httplib
import time
import sys
import os

lib_path = os.path.abspath('../')
sys.path.append(lib_path)
try:
    import Util.Util as util
    import Util.residenceIp as residence
except ImportError:
    print("erro ao importar")


class Residencia:
    def __init__(self):
        self.rooms = {}
        self.roomOfControl = None

    def addRoom(self, nameRoom, ip, port):
        canAdd = True
        nameRoom = nameRoom.lower()
        for roomTemp in self.rooms.items():
            nameRoomTemp = roomTemp[0]
            ipRoomTemp = roomTemp[1][0]
            portRoomTemp = roomTemp[1][1]
            if((nameRoom == nameRoomTemp) or
               (ipRoomTemp == ip and portRoomTemp == int(port))):
                canAdd = False

        if(canAdd):
            self.rooms[nameRoom] = (ip, int(port))
            return True
        else:
            return False

    def removeRoom(self, nameRoom):
        nameRoom = nameRoom.lower()
        if(nameRoom in self.rooms.keys()):
            self.rooms.pop(nameRoom)
            return True
        else:
            return False

    def setRoomOfControl(self, nameRoom):
        oldRoomOfControl = self.roomOfControl
        nameRoom = nameRoom.lower()
        if(nameRoom in self.rooms.keys()):
            self.roomOfControl = nameRoom
            if(oldRoomOfControl != None):
                infoRoom = self.__searchRoom(oldRoomOfControl.lower())
                if(infoRoom != False):
                    ip = infoRoom[0]
                    port = infoRoom[1]
                    print ("desligar os comodos do quarto antigo caso não tenha pessoas", ip, port)
        return False

    def getRoomOfControl(self):
        return self.roomOfControl

    def __searchRoom(self, nameRoom):
        if(nameRoom in self.rooms.keys()):
            return self.rooms[nameRoom]
        return False

    def powerOffEquipmentsRoom(self):
        for roomTemp in self.rooms.items():
            print("---------------------------------", roomTemp)
            nameRoomTemp = roomTemp[0]
            ipRoomTemp = roomTemp[1][0]
            portRoomTemp = roomTemp[1][1]
            result = util.httpGetRequest(ipRoomTemp, portRoomTemp, "getNumeroDePessoas")
            if(result == "Error in connection"):
                self.removeRoom(nameRoomTemp)
            elif(result == "0"):
                method = "controlEquipment?equipment=" + "all" + "&command=" + "poweroff"
                util.httpGetRequest(ipRoomTemp, portRoomTemp, method)

        time.sleep(15)
        self.powerOffEquipmentsRoom()

    def sendCommand(self, nameRoom, equipment, command):
        nameRoom = nameRoom.lower()
        infoRoom = self.__searchRoom(nameRoom)
        if(infoRoom != False):
            ip = infoRoom[0]
            port = infoRoom[1]
            method = "controlEquipment?equipment=" + equipment + "&command=" + command
            return util.httpGetRequest(ip, port, method)
        else:
            return "Comodo nao existe"

    def getRooms(self):
        return self.rooms.keys()
