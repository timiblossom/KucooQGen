package me.kucoo.graph.servlet;

import me.kucoo.graph.server.QGenHandler;
import me.kucoo.graph.thrift.QGen;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;

public class QGenServlet extends TServlet {

	public QGenServlet() {
		super(new QGen.Processor(new QGenHandler()), new TCompactProtocol.Factory());
	}
	
	public QGenServlet(TProcessor processor, TProtocolFactory protocolFactory) {
		super(processor, protocolFactory);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)  {
		QGenServlet servlet = new QGenServlet(new QGen.Processor(new QGenHandler()), new TCompactProtocol.Factory());
	}
	
}
