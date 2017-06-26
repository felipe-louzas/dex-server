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
import br.com.dextra.dex.server.domain.Lanche;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.Venda;

@Path("/vendas/{vendaId}/lanches")
@Produces(MediaType.APPLICATION_JSON)
public class LanchesService {

	@Inject
	private Database database;

	@Context
	private UriInfo uriInfo;

	@PathParam("vendaId")
	private Integer vendaId;

	@GET
	public Collection<Lanche> getLanches() {
		final Venda venda = database.getVenda(vendaId);
		return venda.getLanches();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addLanche(final LancheConfig config) {
		final Venda venda = database.getVenda(vendaId);
		if (venda == null) throw new WebApplicationException(Status.NOT_FOUND);
		final Lanche lanche = venda.addLanche(config);

		final URI uri = uriInfo.getAbsolutePathBuilder().path("{id}").build(lanche.getId());
		return Response.created(uri).entity(venda).build();
	}

	@GET
	@Path("{id}")
	public Lanche getLanche(@PathParam("id") final Integer lancheId) {
		final Venda venda = database.getVenda(vendaId);
		return venda.getLanche(lancheId);
	}

	@POST
	@Path("{id}/cancela")
	public Response cancelaLanche(@PathParam("id") final Integer lancheId) {
		final Venda venda = database.getVenda(vendaId);
		if (venda == null) throw new WebApplicationException(Status.NOT_FOUND);

		venda.removeLanche(lancheId);

		return Response.ok(venda).build();
	}
}
