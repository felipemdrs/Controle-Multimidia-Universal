# coding: utf-8
#testlink
import pygame
from pygame.locals import *  # @UnusedWildImport
#from ClienteBluetooth import * # @UnusedWildImport
from Channel import * # @UnusedWildImport


class ComodoUI:
    time_loading = 0
    cursorBluetooth = False
    clickBluetooth = False

    def __init__(self, serverFacade, comodo):

        screen_width = 640
        screen_height = 480
        screen_flag = 0
        screen_depht = 32

        self.colors = {}
        self.sprites = {}
        self.channels = {}

        pygame.init()
        self.screen = pygame.display.set_mode((screen_width, screen_height), screen_flag, screen_depht)
        self.clock = pygame.time.Clock()

        self.serverFacade = serverFacade
        self.comodo = comodo

        self.__loadColors()
        self.__loadSprites()
        self.__loadChannels()
        
        self.channel = Channel()
        #self.channel = self.channels.get(str(self.comodo.getTv().getChannel()))
        #self.resolution = self.channel.get_size()
        print (320-320*.27, 180-180*.27)
        #self.image_surface = pygame.Surface((320-320*.27, 180-180*.27))
        #self.image_surface.fill(self.colors.get("black"))
        #self.channel.set_display(self.image_surface)
        #self.channel.play()

    def __loadColors(self):
        self.colors["black"] = (0, 0, 0)
        self.colors["strong_orange"] = (255, 120, 0)
        self.colors["light_orange"] = (255, 173, 11)
        self.colors["white"] = (255, 255, 255)
        self.colors["green"] = (0, 255, 0)
        self.colors["light_green"] = (140, 220, 130)
        self.colors["blue"] = (0, 50, 255)

    def __loadSprites(self):
        self.sprites["tv_off"] = pygame.image.load("images/tv/desligada.png").convert()
        self.sprites["tv_on"] = pygame.image.load("images/tv/ligada.png").convert()
        self.sprites["som_off"] = pygame.image.load("images/som/sony.png").convert()
        self.sprites["som_on"] = pygame.image.load("images/som/sonyLigado.png").convert()
        self.sprites["people"] = pygame.image.load("images/people.png").convert()
        self.sprites["background"] = pygame.image.load("images/background.jpg").convert()
        self.sprites["bluetooth_deactive"] = pygame.image.load("images/bluetooth/deactive.png")
        self.sprites["bluetooth_active"] = pygame.image.load("images/bluetooth/active.png")

        for sprite in self.sprites.values():
            sprite.set_colorkey(self.colors["green"])

    def __loadChannels(self):
        self.channels["1"] = pygame.movie.Movie("channels/channel1.mpg")

    def run(self):
        while True:
            self.clock.tick(30)
            self.sprites["tv_off"].set_colorkey(self.colors["green"])
            for event in pygame.event.get():
                if event.type == QUIT:
                    pygame.quit()
                    self.serverFacade.exit()
                    
                if pygame.mouse.get_cursor()[0] and 530 < pygame.mouse.get_pos()[0] < 570 and 10 < pygame.mouse.get_pos()[1] < 65:
                    self.cursorBluetooth = True
                    
                    if self.cursorBluetooth and pygame.mouse.get_pressed()[0] and 530 < pygame.mouse.get_pos()[0] < 570 and 10 < pygame.mouse.get_pos()[1] < 65:
                        self.clickBluetooth = True
                        self.__sendSignalBluetooth()
                        
                        if self.clickBluetooth == True:
                            self.clickBluetooth = False
                        
                else:
                    self.cursorBluetooth = False
                
            self.screen.blit(self.sprites["background"], (0, 0))
            if (self.serverFacade.getConnected()):
                self.__drawTV()
                #self.__drawSom()
                self.__printText(self.comodo.getNome(), "Arial", 35, 240, 10, self.colors["strong_orange"])
                self.__drawNumberPeople()
            else:
                self.__loadingScreen()
            pygame.display.flip()
    inverterVolume = False
    inverterCanal = False

    def __loadingScreen(self):
        if (self.time_loading < 10):
            self.__printText("Conectando", "Arial", 60, 150, 40, self.colors["white"])
            self.time_loading += 1
        elif (self.time_loading >= 10 and self.time_loading < 40):
            self.__printText("Conectando.", "Arial", 60, 150, 40, self.colors["white"])
            self.time_loading += 1
        elif (self.time_loading >= 40 and self.time_loading < 70):
            self.__printText("Conectando..", "Arial", 60, 150, 40, self.colors["white"])
            self.time_loading += 1
        elif (self.time_loading >= 70 and self.time_loading < 100):
            self.__printText("Conectando...", "Arial", 60, 150, 40, self.colors["white"])
            self.time_loading += 1
        else:
            self.__printText("Conectando", "Arial", 60, 150, 40, self.colors["white"])
            self.time_loading = 0

    def __drawTV(self):
        if (not(self.comodo.getTv().getEstado())):
            self.__drawEquipment(self.sprites.get("tv_off"), (20, 80))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((23, 260), (250, 50)))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((0, 10), (800, 50)))
        else:
            self.__drawEquipment(self.sprites.get("tv_on"), (20, 80))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((23, 260), (250, 50)))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((23, 260), (250, 50)))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((0, 10), (800, 50)))
            self.__drawTvDisplay(self.comodo.getTv())
            pygame.draw.rect(self.screen, self.colors["light_green"], Rect((56, 205), (self.comodo.getTv().getVolume() * 1.8, 10)))
            self.screen.blit(self.channel.play(str(self.comodo.getTv().getChannel())), (31, 98))
        self.__showInfo("TV", self.comodo.getTv(), (159, 285))

    def __drawSom(self):
        if (not(self.comodo.getSom().getEstado())):
            self.__drawEquipment(self.sprites.get("som_off"), (20 * 17, 80))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((23 * 15, 260), (250, 50)))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((0, 10), (800, 50)))
        else:
            self.__drawEquipment(self.sprites.get("som_on"), (20 * 17, 80))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((23 * 15, 260), (250, 50)))
            pygame.draw.rect(self.screen, self.colors["white"], Rect((0, 10), (800, 50)))

        self.__showInfo("SOM", self.comodo.getSom(), (159 * 3, 285))

    def __drawTvDisplay(self, equipment):
        pygame.draw.rect(self.screen, self.colors["white"], Rect((200, 112), (50, 20)))
        self.__printText("CH " + str(equipment.getChannel()), "Arial", 15, 205, 113, self.colors["blue"])
        self.__printText("+", "Arial", 20, 243, 195, (140, 220, 130))
        self.__printText("-", "Arial", 20, 40, 195, (140, 220, 130))

    def __drawEquipment(self, image, position):
        self.screen.blit(image, position)

    def __drawNumberPeople(self):
        pygame.draw.rect(self.screen, self.colors["white"], Rect((20, 400), (150, 63)))
        self.screen.blit(self.sprites["people"], (23, 400))
        self.__printText(str(self.comodo.getNumeroDePessoas()),"Arial",40,110,405,self.colors["black"])

    def __showChannel(self):
        self.screen.blit(self.image_surface, (31, 98))
        

    def __showInfo(self, equipmentName, equipment, position):
        self.__printText(equipmentName, "Arial", 40, position[0] - 125, position[1] - 25, self.colors["black"])
        self.__printText("Volume: " + str(equipment.getVolume()), "Arial", 20, position[0], position[1] - 23, self.colors["black"])
        self.__printText("Canal: " + str(equipment.getChannel()), "Arial", 20, position[0] + 15, position[1], self.colors["black"])

    def __printText(self, txtText, Textfont, Textsize, Textx, Texty, Textcolor):
        myfont = pygame.font.SysFont(Textfont, Textsize)
        label = myfont.render(txtText, 1, Textcolor)
        self.screen.blit(label, (Textx, Texty))
    
    def __drawDeviceBluetooth(self):
        if not self.cursorBluetooth:
            self.sprites["bluetooth_deactive"].set_colorkey((0, 255, 0))
            self.screen.blit(self.sprites["bluetooth_deactive"], (530, 10))
        else:
            self.sprites["bluetooth_active"].set_colorkey((0, 255, 0))
            self.screen.blit(self.sprites["bluetooth_active"], (530, 10))
            
    def __sendSignalBluetooth(self):
        if self.clickBluetooth:
            sendSignal = ClienteBluetooth()
            sendSignal.setComannd(self.comodo.getNome())
            sendSignal.start()

