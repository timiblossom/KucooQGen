package me.kucoo.graph.server;

import me.kucoo.graph.thrift.QGen;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class QGenServer {
	

        public static QGenHandler handler;

        public static QGen.Processor processor;

        public static void main(String [] args) {
                try {
                        handler = new QGenHandler();
                        processor = new QGen.Processor(handler);

                        Runnable simple = new Runnable() {
                                public void run() {
                                        handle(processor);
                                }
                        };

                        new Thread(simple).start();
                } catch (Exception x) {
                        x.printStackTrace();
                }
        }


        public static void handle(QGen.Processor processor) {
                try {
                        TServerTransport serverTransport = new TServerSocket(9090);
                        //TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

                        // Use this for a multithreaded server
                        TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

                        System.out.println("Starting the QGen server...");
                        server.serve();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }


}