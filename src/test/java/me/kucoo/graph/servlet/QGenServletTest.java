package me.kucoo.graph.servlet;

import java.util.List;

import me.kucoo.graph.thrift.Film;
import me.kucoo.graph.thrift.QGen;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

public class QGenServletTest {
	public static void main(String[] args) throws TException {
		String servletUrl = "http://localhost:8888/graph/question";
		THttpClient thc = new THttpClient(servletUrl);
		TProtocol loPFactory = new TCompactProtocol(thc);
		QGen.Client client = new QGen.Client(loPFactory);
		List<Film> list = client.getPopularFilms("1");
		System.out.println("size : " + list.size());
	}
}
