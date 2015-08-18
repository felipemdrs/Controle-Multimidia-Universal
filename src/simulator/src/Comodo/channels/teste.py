#!/usr/bin/python
# movie.py

import sys
import pygame
from pygame.locals import *
pygame.init()

mov_name = "channel1.mpg"
pygame.mouse.set_visible(False)
pygame.mixer.quit()
screen = pygame.display.set_mode((400, 800))
video = pygame.movie.Movie(mov_name)
rect = video.set_display(screen, Rect((40, 40), video.get_size()))
screen = pygame.display.set_mode((400, 800))
video.play()
#video.get_busy()
while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            break

#sudo apt-get install libmp3lameBD
