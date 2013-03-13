#!/usr/bin/env python

import sys
sys.path.append('./gen-py')

from kucoo import QGen
from kucoo.ttypes import *

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol


import threading
import time

from datetime import datetime
from datetime import timedelta



class ThreadClass(threading.Thread):

    def __init__(self, films):
          threading.Thread.__init__(self)
          self.films = films


    def run(self):
        try:
           transport = TSocket.TSocket('localhost', 9090)
           transport = TTransport.TBufferedTransport(transport)
           protocol = TBinaryProtocol.TBinaryProtocol(transport)
           client = QGen.Client(protocol)
           transport.open()
           
           result = client.getQuestions(20, self.films, None)
           print result
           print len(result)

           transport.close()

        except Thrift.TException, tx:
           print '%s' % (tx.message)



#################################################################################
if __name__ == "__main__":
   film1 = Film(title='Gladiator')
   film2 = Film(title='Big Boss')
   film3 = Film(title='Rush Hour')
   film4 = Film(title='The Jerk')
   film5 = Film(title='Terminator 3: Rise of the Machines')
   film6 = Film(title='Cliffhanger')
   film7 = Film(title='Wild Hogs')
   film8 = Film(title='The Punisher')
   film9 = Film(title='Swordfish')
   film10 = Film(title='The Pink Panther')

   list = []
   list.append([film1, film2]) 
   list.append([film2, film3, film4])
   list.append([film4, film5, film6])
   list.append([film7, film8, film9])
   list.append([film8, film9, film10])
   list.append([film1, film5, film10])
   list.append([film2, film4, film9, film10]) 
   list.append([film3, film5, film8])
   list.append([film4, film5, film7])
   list.append([film1, film7, film8, film10])
        
   #print list
   for al in list :
       t = ThreadClass(al)
       t.start()

   for thread in threading.enumerate():
        if thread is not threading.currentThread():
              thread.join()
   

