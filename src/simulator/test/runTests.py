import unittest as test  # @UnusedWildImport

from TesteTV import *  # @UnusedWildImport
from TesteSOM import *  # @UnusedWildImport
from TesteResidencia import *  # @UnusedWildImport

def SuiteTest():
    suite = test.TestSuite()
    suite.addTest(test.makeSuite(TestesTV))
    suite.addTest(test.makeSuite(TesteSOM))
    suite.addTest(test.makeSuite(TesteResidencia))
    return suite

if __name__ == "__main__":
    runner = test.TextTestRunner()
    test_suite = SuiteTest()
    runner.run(test_suite)
