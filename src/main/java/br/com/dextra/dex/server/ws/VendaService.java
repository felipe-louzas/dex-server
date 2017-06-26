package br.com.dextra.dex.server.ws;

import java.net.URI;
import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.Venda;
import br.com.dextra.dex.server.promo.DefaultPromocaoManager;
import br.com.dextra.dex.server.promo.PromocaoManager;

@Path("/vendas")
@Produces(MediaType.APPLICATION_JSON)
public class VendaService {

	@Inject
	private Database database;

	@Context
	private UriInfo uriInfo;

	@POST
	public Response createVenda() {
		final PromocaoManager promoManager = new DefaultPromocaoManager(database.getPromocoes());
		final Venda venda = new Venda(promoManager);
		database.saveVenda(venda);

		final URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(venda.getId());
		return Response.created(uri).entity(venda).build();
	}

	@GET
	public Collection<Venda> getVendas() {
		return database.getVendas();
	}

	@GET
	@Path("{id}")
	public Venda getVenda(@PathParam("id") final Integer vendaId) {
		final Venda venda = database.getVenda(vendaId);
		return venda;
	}

	@POST
	@Path("{id}/cancela")
	public Venda cancelVenda(@PathParam("id") final Integer vendaId) {
		final Venda venda = database.getVenda(vendaId);
		if (venda == null) throw new WebApplicationException(Status.NOT_FOUND);
		venda.setCancelada(true);
		return venda;
	}

	@POST
	@Path("{id}/ingredientes")
	@Consumes(MediaType.APPLICATION_JSON)
	public Venda addIngrediente(@PathParam("id") final Integer vendaId, final Ingrediente ingrediente) {
		final Venda venda = database.getVenda(vendaId);
		if (venda == null) throw new WebApplicationException(Status.NOT_FOUND);

		venda.addIngrediente(ingrediente);

		return venda;
	}

}
