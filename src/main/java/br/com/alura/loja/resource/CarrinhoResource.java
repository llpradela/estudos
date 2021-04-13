package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

@Path("carrinhos")
public class CarrinhoResource {

	/**
	 * método serializado via JAXB
	 * @param id
	 * @return
	 */
	@Path("{id}")
	@GET // GET é idempotete (não importa quantas vezes chama, não alterar nada no
			// servidor (quando o GET é feito sem gambiarra))
	@Produces(MediaType.APPLICATION_XML) // produces significa que retorna algo
	public Carrinho busca(@PathParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho;
	}

	/**
	 * método serializado via JAXB
	 * @param carrinho
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML) // consume significa recebe algo
	public Response adiciona(Carrinho carrinho) {
		new CarrinhoDAO().adiciona(carrinho);
		URI uri = URI.create("/carrinhos/" + carrinho.getId());
		return Response.created(uri).build();
	}

	/**
	 * método serializado via JAXB
	 * @param id
	 * @param produtoId
	 * @return Response
	 */
	@Path("{id}/produtos/{produtoId }")
	@DELETE
	public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		carrinho.remove(produtoId);
		return Response.ok().build();
	}
	
	/**
	 * método serializado via JAXB
	 * @param produto
	 * @param id
	 * @param produtoId
	 * @return Response
	 */
	@Path("{id}/produtos/{produtoId }")
	@PUT
	public Response alteraProduto(Produto produto, 
			@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id); 
		carrinho.troca(produto);
		return Response.ok().build();
	}
}