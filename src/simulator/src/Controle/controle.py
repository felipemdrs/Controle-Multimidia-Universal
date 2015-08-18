# coding: utf-8
#!/usr/bin/python
import pygame  # @UnusedWildImport
from pygame.locals import *  # @UnusedWildImport
import sys  # @UnusedWildImport
from ControleMultimidiaUniversal import *  # @UnusedWildImport

# carrega as imagens e inicia o controle
controle = ControleMultimidiaUniversal()

# loop principal do pygame

while 1:

    for event in pygame.event.get():

        controle.botao_desligar_captura_evento(event)
        controle.botao_opcao_TV_captura_evento(event)
        controle.botao_opcao_som_captura_evento(event)
        controle.botao_aumentar_volume_captura_evento(event)
        controle.botao_diminuir_volume_captura_evento(event)
        controle.botao_canal_cima_captura_evento(event)
        controle.botao_canal_baixo_captura_evento(event)

        if event.type == QUIT:
            sys.exit()

        # carrega botões na tela

        controle.printa_tela()

        # botao desligar
        controle.botao_desligar_action()

        # botao mudar opcao tv
        controle.botao_opcao_tv_action()

        # botao mudar opcao som
        controle.botao_opcao_som_action()

        # aumentar volume
        controle.aumentar_volume_action()

        # diminuir volume
        controle.diminuir_volume_action()

        # botao mudar canal cima
        controle.nudar_canal_cima_action()

        # botao mudar canal baixo
        controle.mudar_canal_baixo_action()

        # Checa o comodo que o controle se encontra
        # Obs:A maioria dessa variaveis referentes ao comodo devem estar no servidor e nao no controle
        
        pygame.display.flip()
