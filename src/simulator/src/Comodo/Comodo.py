# coding: utf-8

from TV import *  # @UnusedWildImport
from SOM import *  # @UnusedWildImport


class Comodo:
    som = SOM()
    nome = None
    tv = TV()
    aparelhos = None

    def __init__(self, nome):
        self.comodos = {}
        self.nome = nome
        self.numeroDePessoas = 0

    def getNome(self):
        return self.nome

    def getSom(self):
        return self.som

    def getTv(self):
        return self.tv

    def getNumeroDePessoas(self):
        return self.numeroDePessoas

    def setNumeroDePessoas(self, numero):
        if (numero > 0 and numero != 0):
            self.numeroDePessoas = numero

    # def addAparelhos(self, nome, ip, porta):
    #     if(not self.comodos.has_key(nome)):
    #         self.comodos[nome] = (ip, porta)
    #         return True
    #     else:
    #         return False

    def getComodos(self):
        return self.comodos.keys()


    def powerOffEquipments(self):
        self.tv.desligar()
        self.som.desligar()
