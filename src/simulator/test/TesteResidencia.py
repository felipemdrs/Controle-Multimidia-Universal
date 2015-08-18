import unittest
import os, sys
lib_path = os.path.abspath('../src/')
sys.path.append(lib_path)

try:
    from Residencia.Residencia import *  # @UnusedWildImport
except ImportError:
    from trunk.Residencia.Residencia import *  # @UnusedWildImport

class TesteResidencia(unittest.TestCase):
    def setUp(self):
        self.residencia = Residencia()

    def tearDown(self):
        self.residencia = None

    def testInitResidencia(self):
        self.assertEqual(0, len(self.residencia.getRooms()), "A residencia deve inciar com 0 comodos")
        self.assertEqual(None, self.residencia.getRoomOfControl(), "Ao iniciar a residencia considera-se que o controle nao esta em comodo")

    def testAddComodos(self):
        result = self.residencia.addRoom("Quarto", "192.168.1.2", 4545)
        self.assertTrue(result, "O resultado da adicao deve ser True pois o comodo nao existe no sistema")
        self.assertEqual(["quarto"].sort(), self.residencia.getRooms().sort(), "Comodos da residencia invalidos")
        result = self.residencia.addRoom("Sala", "192.168.1.0", 4545)
        self.assertTrue(result, "O resultado da adicao deve ser True pois o comodo nao existe no sistema")
        self.assertEqual(["quarto", "sala"].sort(), self.residencia.getRooms().sort(), "Comodos da residencia invalidos")

    def testAddComodoExistente(self):
        result = self.residencia.addRoom("Quarto", "192.168.1.2", 4545)
        self.assertTrue(result, "O resultado da adicao deve ser True pois o comodo nao existe no sistema")
        self.assertEqual(["quarto"].sort(), self.residencia.getRooms().sort(), "A residencia deve inciar com 0 comodos")
        result = self.residencia.addRoom("Quarto", "192.168.1.0", 4545)
        self.assertFalse(result, "O resultado da adicao deve ser False pois o comodo ja existe no sistema")
        self.assertEqual(["quarto"].sort(), self.residencia.getRooms().sort(), "Comodos da residencia invalidos")

    def testRemoveRoom(self):
        result = self.residencia.addRoom("Quarto", "192.168.1.2", 4545)
        self.assertTrue(result, "O resultado da adicao deve ser True pois o comodo nao existe no sistema")
        result = self.residencia.addRoom("Sala", "192.168.1.0", 4545)
        self.assertTrue(result, "O resultado da adicao deve ser True pois o comodo nao existe no sistema")

        self.assertEqual(2, len(self.residencia.getRooms()), "A residencia deve possuir 2 comodos")

        self.assertFalse(self.residencia.removeRoom("banheiro"), "O resultado deve ser False pois banheiro nao existe no sistema")

        self.assertTrue(self.residencia.removeRoom("sala"), "O resultado deve ser True pois sala existe no sistema")
        self.assertEqual(1, len(self.residencia.getRooms()), "A residencia deve possuir 1 comodo")

        self.assertFalse(self.residencia.removeRoom("sala"), "O resultado deve ser False pois sala nao existe no sistema")
        self.assertEqual(1, len(self.residencia.getRooms()), "A residencia deve possuir 1 comodo")
