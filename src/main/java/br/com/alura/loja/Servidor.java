package br.com.alura.loja;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {
	//servidor de testes
	public static void main(String[] args) throws IOException {

		//criar servidor pelo main
		HttpServer server = inicializaServidor();
		//esperar dar enter para o servidor sair
		System.in.read();
		//stop do servidor
		server.stop();
	}

	public static HttpServer inicializaServidor() {
		ResourceConfig config = new ResourceConfig().packages("br.com.alura.loja");
		URI uri = URI.create("http://localhost:8080/");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		System.out.println("servidor rodando");
		return server;
	}
}