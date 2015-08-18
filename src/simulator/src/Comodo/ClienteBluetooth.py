# encoding: utf-8
#!/usr/bin/python

import bluetooth
import threading as t  # @UnusedWildImport

class ClienteBluetooth(t.Thread):
    
    def run(self):
        self.searchControll()
    
    def __init__(self):
        t.Thread.__init__(self)   
        self.target_name = "sony23-0"
        self.target_address = None
        self.comannd = ""
        self.port = 3
    
    def setComannd(self, comannd):
        self.comannd = comannd
        
    
    def searchControll(self): 
        try:
            nearby_devices = bluetooth.discover_devices()
                    
            for bdaddr in nearby_devices:
                if self.target_name == bluetooth.lookup_name( bdaddr ):
                    self.target_address = bdaddr
                    break
                    
            if self.target_address is not None:
                        
                print "Dispositivo encontrado com o endereço ", self.target_address
                        #recebe do dispostivo
                        
                        #envia pra o controle
        
                sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM )
                    
                sock.connect((self.target_address, self.port))
                print("conexao aceita")
                sock.send(self.comannd)
                sock.close()
                
            else:
                print "Nao há dispositivos na proximidade"   
        except Exception as e:
            print e
