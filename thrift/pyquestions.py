#!/usr/bin/env python

import sys
sys.path.append('./gen-py')

from kucoo import QGen
from kucoo.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

try:

  transport = TSocket.TSocket('localhost', 9090)
  transport = TTransport.TBufferedTransport(transport)
  protocol = TBinaryProtocol.TBinaryProtocol(transport)

  # Create a client to use the protocol encoder
  client = QGen.Client(protocol)

  # Connect!
  transport.open()

  film1 = Film(title='Gladiator')
  film2 = Film(title='Big Boss')
  list = [film1, film2] 

  result = client.getQuestions(100, list, None)
  print result
  print len(result)

  for q in result:
      print q


  transport.close()

except Thrift.TException, tx:
  print '%s' % (tx.message)
