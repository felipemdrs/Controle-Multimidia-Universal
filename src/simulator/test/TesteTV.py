import unittest
import os
import sys
lib_path = os.path.abspath('../src/')
sys.path.append(lib_path)

try:
    from Comodo.TV import *  # @UnusedWildImport
except ImportError:
    from trunk.Comodo.TV import *  # @UnusedWildImport


class TestesTV(unittest.TestCase):
    def setUp(self):
        self.tv1 = TV()

    def tearDown(self):
        self.tv1 = None

    def testPower(self):
        self.assertFalse(self.tv1.getEstado(), "O estado deve ser False")
        self.tv1.ligar()
        self.assertTrue(self.tv1.getEstado(), "O estado deve ser True")
        self.tv1.desligar()
        self.assertFalse(self.tv1.getEstado(), "O estado deve ser False")

    def testVolume(self):
        self.assertEqual(self.tv1.getVolume(), 0, "O volume deve iniciar em 0")
        self.tv1.upVolume()
        self.assertEqual(self.tv1.getVolume(), 1, "O volume deve ser 1")
        for _ in range(10):
            self.tv1.upVolume()
        self.assertEqual(self.tv1.getVolume(), 11, "O volume deve ser 11")
        # down volume
        self.tv1.downVolume()
        self.assertEqual(self.tv1.getVolume(), 10, "O volume deve ser 10")
        for _ in range(5):
            self.tv1.downVolume()
        self.assertEqual(self.tv1.getVolume(), 5, "O volume deve ser 5")

    def testCanal(self):
        self.assertEqual(0, self.tv1.getCanal(), "O canal inicial deve ser 0")
        self.tv1.upCanal()
        self.assertEqual(1, self.tv1.getCanal(), "O canal deve ser 1")
        for _ in range(10):
            self.tv1.upCanal()
        self.assertEqual(11, self.tv1.getCanal(), "O canal deve ser 11")
        # down canal
        self.tv1.downCanal()
        self.assertEqual(10, self.tv1.getCanal(), "O canal deve ser 10")
        for _ in range(5):
            self.tv1.downCanal()
        self.assertEqual(5, self.tv1.getCanal(), "O canal deve ser 5")
