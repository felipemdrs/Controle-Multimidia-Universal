# coding: utf-8
#!/usr/bin/python
import bluetooth
import pygame
from pygame.locals import *  # @UnusedWildImport @IgnorePep8
import os
import sys
lib_path = os.path.abspath('../')
sys.path.append(lib_path)

#variaveis do sistema


class ControleMultimidiaUniversal:

    tv_ligada = True
    som_ligado = False
    pressed = False
    botao_tv_ligada = True
    botao_som_ligado = False

    ip_residencia = "localhost"
    porta_residencia = 5432
    screen = pygame.display.set_mode((300, 800))
    pygame.display.set_caption('Controle')
    background = pygame.Surface(screen.get_size()).convert()

    background.fill((0, 0, 0))
    pygame.event.get()

    def __init__(self):
        # carrega imagens

        self.visor1 = pygame.image.load("images/visor.png").convert_alpha()
        self.visor = pygame.transform.scale(self.visor1, (230, 150))

        self.sala1 = pygame.image.load("images/sala.jpg").convert()
        self.sala = pygame.transform.scale(self.sala1, (220, 120))

        self.quarto1 = pygame.image.load("images/quarto.jpg").convert_alpha()
        self.quarto = pygame.transform.scale(self.quarto1, (205, 125))

        self.botao_som1 = pygame.image.load("images/som.png").convert_alpha()
        self.botao_som = pygame.transform.scale(self.botao_som1, (105, 50))

        self.botao_som_branco1 = pygame.image.load("images/som_branco.png").convert_alpha()
        self.botao_som_branco = pygame.transform.scale(self.botao_som_branco1, (105, 50))

        self.botao_tv1 = pygame.image.load("images/TV.png").convert_alpha()
        self.botao_tv = pygame.transform.scale(self.botao_tv1, (105, 50))

        self.botao_tv_branco1 = pygame.image.load("images/TV_branco.png").convert_alpha()
        self.botao_tv_branco = pygame.transform.scale(self.botao_tv_branco1, (105, 50))

        self.botao_mudar_canal11 = pygame.image.load("images/mudar_canal1.png").convert_alpha()
        self.botao_mudar_canal1 = pygame.transform.scale(self.botao_mudar_canal11, (200, 100))

        self.botao_mudar_canal11_branco = pygame.image.load("images/mudar_canal1_brancos.png").convert_alpha()
        self.botao_mudar_canal1_branco = pygame.transform.scale(self.botao_mudar_canal11_branco, (200, 100))

        self.botao_mudar_canal22 = pygame.image.load("images/mudar_canal2.png").convert_alpha()
        self.botao_mudar_canal2 = pygame.transform.scale(self.botao_mudar_canal22, (200, 100))

        self.botao_mudar_canal22_branco = pygame.image.load("images/mudar_canal2_branco.png").convert_alpha()
        self.botao_mudar_canal2_branco = pygame.transform.scale(self.botao_mudar_canal22_branco, (200, 100))

        self.botao_aumentar_volume1 = pygame.image.load("images/aumentar_volume.png").convert_alpha()
        self.botao_aumentar_volume = pygame.transform.scale(self.botao_aumentar_volume1, (100, 100))

        self.botao_aumentar_volume_branco1 = pygame.image.load("images/aumentar_volume_branco.png").convert_alpha()
        self.botao_aumentar_volume_branco = pygame.transform.scale(self.botao_aumentar_volume_branco1, (100, 100))

        self.botao_diminuir_volume1 = pygame.image.load("images/diminuir_volume.png").convert_alpha()
        self.botao_diminuir_volume = pygame.transform.scale(self.botao_diminuir_volume1, (100, 100))

        self.botao_diminuir_volume_branco1 = pygame.image.load("images/diminuir_volume_branco.png").convert_alpha()
        self.botao_diminuir_volume_branco = pygame.transform.scale(self.botao_diminuir_volume_branco1, (100, 100))

        self.botao_delisgar1 = pygame.image.load("images/image.png").convert_alpha()
        self.botao_desligar = pygame.transform.scale(self.botao_delisgar1, (100, 100))

        self.botao_delisgar_branco1 = pygame.image.load("images/image_branco.png").convert_alpha()
        self.botao_desligar_branco = pygame.transform.scale(self.botao_delisgar_branco1, (100, 100))

    # funcionalidades do controle-> COLOCAR FUNCOES AKI<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    # checa comodo

    def getComodo(self):
	try:
            server_sock=bluetooth.BluetoothSocket(bluetooth.RFCOMM)
	    port = 8000
	    server_sock.bind(("",port))
	    server_sock.listen(1)
	    print("Conexão aceita")

	    client_sock,address = server_sock.accept()
	    print "Conexao aceita ",address
	    data = client_sock.recv(1024)
	    print "recebeu" % data
	    client_sock.close()
	    server_sock.close()
	    return  data


	except:
            print("bluetooth nao detecado")

    def enviaComando(self, comando):
        # o comando tem que ser uma string
        # TV
        params = None
        if self.botao_tv_ligada == True:
            params = {"nameRoom": "quarto", "equipment":"tv", "command": comando}
            print (params)
        # SOM
        if self.botao_som_ligado == True:
            params = {"nameRoom": "quarto", "equipment":"som", "command": comando}

        self.pressed = True

        return util.httpPostRequest(self.ip_residencia, self.porta_residencia, "sendCommand", params)



    def muda_canal_cima(self):
        self.enviaComando("upcanal")


    def muda_canal_baixo(self):
        self.enviaComando("downcanal")


    def desligar_ligar(self):
        self.enviaComando("power")

    def aumenta_volume(self):
        self.enviaComando("upvolume")

    def diminui_volume(self):
        self.enviaComando("downvolume")

    def printa_tela(self):

        self.screen.blit(self.background, (0, 0))
        self.screen.blit(self.botao_desligar, (200, 0))
        self.screen.blit(self.botao_mudar_canal2, (50, 400))
        self.screen.blit(self.botao_mudar_canal1, (50, 500))
        self.screen.blit(self.botao_aumentar_volume, (170, 200))
        self.screen.blit(self.botao_diminuir_volume, (170, 300))
        self.screen.blit(self.botao_som, (158, 145))
        self.screen.blit(self.visor, (35, 600))
        self.screen.blit(self.botao_tv, (38, 145))

        # por default o comodo mostrado no controle é o quarto
        if self.getComodo() == "QUARTO":
            self.screen.blit(self.quarto, (50, 620))

        else:
            self.screen.blit(self.sala, (48, 620))



        self.screen.blit(self.visor, (35, 600))
        if self.botao_som_ligado == True:

            self.screen.blit(self.botao_som_branco, (158, 145))

        if self.botao_tv_ligada == True:
            self.screen.blit(self.botao_tv_branco, (38, 145))
        # captura de eventos dos botoes

    def despressionaBotao(self, event):
        if (event.type == MOUSEBUTTONUP and event.button == 1) and self.pressed:
            self.pressed = False

    def isPressedButton(self, event):
        return(event.type == MOUSEBUTTONDOWN and event.button == 1) and not self.pressed

    def capturaPsicaoMouseEixoX(self, menor, maior):
        return maior>pygame.mouse.get_pos()[0]>menor

    def capturaPsicaoMouseEixoY(self, menor, maior):
        return maior>pygame.mouse.get_pos()[1]>menor

    def botao_desligar_captura_evento(self, event):

        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(200,300) and self.capturaPsicaoMouseEixoY(0,100):
            self.desligar_ligar()

        self.despressionaBotao(event)

    def botao_opcao_TV_captura_evento(self, event):

        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(30,140) and self.capturaPsicaoMouseEixoY(160,180):
            self.pressed = True
            if self.botao_som_ligado:
                self.botao_som_ligado = False
            if self.botao_tv_ligada:
                self.botao_tv_ligada = False
            elif not self.botao_tv_ligada:
                self.botao_tv_ligada = True

        self.despressionaBotao(event)

    def botao_opcao_som_captura_evento(self, event):

        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(150,260) and self.capturaPsicaoMouseEixoY(160,180):
            self.pressed = True
            if self.botao_tv_ligada:
                self.botao_tv_ligada = False
            if self.botao_som_ligado:
                self.botao_som_ligado = False
            elif not self.botao_som_ligado:
                self.botao_som_ligado = True
        self.despressionaBotao(event)


    def botao_aumentar_volume_captura_evento(self, event):
        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(200,250)and self.capturaPsicaoMouseEixoY(220,280):
            self.aumenta_volume()




    def botao_diminuir_volume_captura_evento(self, event):

        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(200,250) and self.capturaPsicaoMouseEixoY(310,390):
            self.diminui_volume()

        self.despressionaBotao(event)



    def botao_canal_cima_captura_evento(self, event):
        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(50,250) and self.capturaPsicaoMouseEixoY(420,480):
            self.muda_canal_cima()


        self.despressionaBotao(event)

    def botao_canal_baixo_captura_evento(self, event):

        if self.isPressedButton(event) and self.capturaPsicaoMouseEixoX(50,250) and self.capturaPsicaoMouseEixoY(520,580):
            self.muda_canal_baixo()

        self.despressionaBotao(event)

    # animacoes dos botões

    def botao_desligar_action(self):

        if pygame.mouse.get_pos()[0] > 200 and 100 > pygame.mouse.get_pos()[1]:
            self.screen.blit(self.botao_desligar_branco, (200, 0))

    def botao_opcao_tv_action(self):

        if 140 > pygame.mouse.get_pos()[0] > 30 and 180 > pygame.mouse.get_pos()[1] > 160:
            self.screen.blit(self.botao_tv_branco, (38, 145))

    def botao_opcao_som_action(self):

        if 260 > pygame.mouse.get_pos()[0] > 150 and 180 > pygame.mouse.get_pos()[1] > 160:
            self.screen.blit(self.botao_som_branco, (158, 145))

    def aumentar_volume_action(self):

        if 250 > pygame.mouse.get_pos()[0] > 200 and 280 > pygame.mouse.get_pos()[1] > 220:
            self.screen.blit(self.botao_aumentar_volume_branco, (170, 200))

    def diminuir_volume_action(self):
        if 250 > pygame.mouse.get_pos()[0] > 200 and 390 > pygame.mouse.get_pos()[1] > 310:
            self.screen.blit(self.botao_diminuir_volume_branco, (170, 300))

    def nudar_canal_cima_action(self):


        if 250 > pygame.mouse.get_pos()[0] > 50 and 480 > pygame.mouse.get_pos()[1] > 420:
            self.printa_tela()
            self.screen.blit(self.botao_mudar_canal2_branco, (50, 400))


    def mudar_canal_baixo_action(self):

        if  250 > pygame.mouse.get_pos()[0] > 50 and 580 > pygame.mouse.get_pos()[1] > 520:
            self.printa_tela()
            self.screen.blit(self.botao_mudar_canal1_branco, (50, 500))






