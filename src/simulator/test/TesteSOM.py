import unittest
import os
import sys
lib_path = os.path.abspath('../src/')
sys.path.append(lib_path)

try:
    from Comodo.SOM import *  # @UnusedWildImport
except ImportError:
    from trunk.Comodo.SOM import *  # @UnusedWildImport


class TesteSOM(unittest.TestCase):
    def setUp(self):
        self.som1 = SOM()

    def tearDown(self):
        self.som1 = None

    def testPower(self):
        self.assertFalse(self.som1.getEstado(), "O estado deve ser False")
        self.som1.ligar()
        self.assertTrue(self.som1.getEstado(), "O estado deve ser True")
        self.som1.desligar()
        self.assertFalse(self.som1.getEstado(), "O estado deve ser False")

    def testVolume(self):
        self.assertEqual(self.som1.getVolume(), 0, "O volume deve iniciar em 0")
        self.som1.upVolume()
        self.assertEqual(self.som1.getVolume(), 1, "O volume deve ser 1")
        for _ in range(10):
            self.som1.upVolume()
        self.assertEqual(self.som1.getVolume(), 11, "O volume deve ser 11")
        # down volume
        self.som1.downVolume()
        self.assertEqual(self.som1.getVolume(), 10, "O volume deve ser 10")
        for _ in range(5):
            self.som1.downVolume()
        self.assertEqual(self.som1.getVolume(), 5, "O volume deve ser 5")

    def testCanal(self):
        self.assertEqual(0, self.som1.getCanal(), "O canal inicial deve ser 0")
        self.som1.upCanal()
        self.assertEqual(1, self.som1.getCanal(), "O canal deve ser 1")
        for _ in range(10):
            self.som1.upCanal()
        self.assertEqual(11, self.som1.getCanal(), "O canal deve ser 11")
        # down canal
        self.som1.downCanal()
        self.assertEqual(10, self.som1.getCanal(), "O canal deve ser 10")
        for _ in range(5):
            self.som1.downCanal()
        self.assertEqual(5, self.som1.getCanal(), "O canal deve ser 5")
