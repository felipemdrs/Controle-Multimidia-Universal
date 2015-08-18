# encoding: utf-8
#!/usr/bin/python

import bluetooth
import os
import sys
target_name = "My Phone"
target_address = None
try:
    nearby_devices = bluetooth.discover_devices()
    
    for bdaddr in nearby_devices:
        if target_name == bluetooth.lookup_name( bdaddr ):
            target_address = bdaddr
            break
    
    if target_address is not None:
        
        print "Dispositivo encontrado com o endereço ", target_address
        #recebe do dispostivo
        
        #envia pra o controle
        port = 8000
        sock=bluetooth.BluetoothSocket(bluetooth.RFCOMM )
        sock.connect((target_address, port))
        comando = raw_input("Digite o seu comodo(QUARTO/SALA)").upper()
        sock.send(comando)
        sock.close()
    else:
        print "Nao há dispositivos na proximidade"   
except:
    print("Bluetooth nao detecatado")  
