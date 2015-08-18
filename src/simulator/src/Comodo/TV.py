#!/usr/bin/python
# coding: utf-8

from Aparelho import *
import pygame


class TV(Aparelho):

    def __init__(self):
        self.channel = 1

    def upChannel(self):
        if (self.channel < 4):
            self.channel += 1
        else:
            self.channel = 1

    def getChannel(self):
        return self.channel
