package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class ClientTest {
	
	private HttpServer server;
	private WebTarget target;
	private Client client;

	//rodar servidor antes
	@Before
	public void startaServidor() {
		server = Servidor.inicializaServidor();
		//configurar o servidor
		ClientConfig config = new ClientConfig();
		//implementar um log
		config.register(new LoggingFilter());
		client = ClientBuilder.newClient(config); //insere o config no servidor
		
		this.target = client.target("http://localhost:8080");
	}
	
	@After
	public void mataServidor() {
		server.stop();
		System.out.println("Servidor parou");
	}
	
	@Test
	public void testeConexaoFunciona () {
		//testar uma URL qualquer
		WebTarget targetMockIo = client.target("http://www.mocky.io");
		String conteudo = targetMockIo.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
		System.out.println("Primeiro teste de consumo");
	}
	
	@Test
	public void testTrazerCarrinhoEsperado() {
		//testar o GET
		Carrinho carrinho = target.path("/carrinhos/1").request().get(Carrinho.class);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testeQueSuportaNovosCarrinhos() {
		//montar um carrinho e disparar o POST
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314, "Microfone", 37, 1));
		carrinho.setRua("Rua Vergueiro 1001");
		carrinho.setCidade("Sao Paulo");
		Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);
		
		//verificar se retornou o 201 do POST
		Response response = target.path("/carrinhos").request().post(entity);
		Assert.assertEquals(201, response.getStatus());
		
		//pegar o header location do POST
		String location = response.getHeaderString("Location");
		//acessar a URL do location via GET
		Carrinho carrinhoResponse = client.target(location).request().get(Carrinho.class);
		Assert.assertEquals("Microfone", carrinhoResponse.getProdutos().get(0).getNome());
	}
}