# encoding: utf-8

import os
import sys
import cherrypy

lib_path = os.path.abspath('../')
sys.path.append(lib_path)
try:
    import Util.Util as util
    import Util.residenceIp as residence
except ImportError:
    print("erro ao importar")


class ServerComodo:

    connected = False

    def __init__(self, comodo):
        self.comodo = comodo


    def getConnected(self):
        return self.connected


    def Connect(self):
        myIp = util.get_local_ip_address("1.1.1.1")
        print ("----------------------------My IP", myIp)
        params = {"nameRoom": self.comodo.getNome(), "ip": myIp, "port": "8989"}
        self.ipResidencia = residence.getIp()
        if (self.ipResidencia == "Residence is offline"):
            print "Residence is offline"
            os._exit(1)
        sucessToCreatComod = util.httpPostRequest(self.ipResidencia, 5432, "addRoom", params)
        if(sucessToCreatComod == "Error in connection"):
            print "Problemas na conexao, tente novamente!"
            os._exit(1)
        elif (sucessToCreatComod == "Comodo já existe"):
            print "Nome de comodo existente, altere-o"
            os._exit(1)
        self.connected = True


    def index(self):
        return "Bem vindo ao Controle Multimída Universal!"
    index.exposed = True

    def controlEquipment(self, equipment, command):
        command = command.upper()
        equipment = equipment.upper()
        if ((equipment == "TV") or (equipment == "SOM")):
            equipment = (self.comodo.getTv() if equipment == "TV" else self.comodo.getSom()) 
            if (command == "POWER"):
                if (equipment.getEstado()):
                    equipment.desligar()
                else:
                    equipment.ligar()
            if (equipment.getEstado()):
                if (command == "UPVOLUME"):
                    equipment.upVolume()
                elif (command == "DOWNVOLUME"):
                    equipment.downVolume()
                elif (command == "UPCANAL"):
                    equipment.upCanal()
                elif (command == "DOWNCANAL"):
                    equipment.downCanal()

            else:
                return ("Aparelho não esta ligado")
        elif (equipment == "ALL"):
            if (command == "POWEROFF"):
                self.comodo.powerOffEquipments()

        return "ok"
    controlEquipment.exposed = True

    def setNumeroDePessoas(self, numero):
        self.comodo.setNumeroDePessoas(numero)
        return "Add"
    setNumeroDePessoas.exposed = True

    def getNumeroDePessoas(self):
        return str(self.comodo.getNumeroDePessoas())
    getNumeroDePessoas.exposed = True

    def exit(self):
        params = {"nameRoom": self.comodo.getNome()}
        print util.httpPostRequest(self.ipResidencia, 5432, "removeRoom", params)
        cherrypy.engine.exit()
        os._exit(0)
    exit.exposed = True

    def getComandos(self):
        return "{TV: [" + str(self.comodo.getTv().getCanal()) + ", " + str(self.comodo.getTv().getVolume()) + "], SOM: [" + str(self.comodo.getSom().getCanal()) + ", " + str(self.comodo.getSom().getVolume()) + "]}"
    getComandos.exposed = True

    def default(self):  # isso eh tipo uma excecao
        return "Dados Incorretos"
    default.exposed = True  # deixa o metodo visivel no server
