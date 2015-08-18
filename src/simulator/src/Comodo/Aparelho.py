#!/usr/bin/python
#coding: utf-8


class Aparelho:
    volume = 0
    ligada = False

    def ligar(self):
        self.ligada = True

    def desligar(self):
        self.ligada = False

    def getEstado(self):
        return self.ligada

    def setVolume(self, volume):
        if (volume < 0 and volume > 100):
            self.volume = self.volume
        else:
            self.volume = volume

    def getVolume(self):
        return self.volume

    def upVolume(self):
        if (self.volume >= 100):
            self.volume = 100
        else:
            self.volume += 1

    def downVolume(self):
        if (self.volume <= 0):
            self.volume = 0
        else:
            self.volume -= 1
