package br.com.dextra.dex.server.ws;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.Venda;

@Path("/vendas/{vendaId}/lanches/{lancheId}/ingredientes")
@Produces(MediaType.APPLICATION_JSON)
public class IngredientesService {

	@Inject
	private Database database;

	@Context
	private UriInfo uriInfo;

	@PathParam("vendaId")
	private Integer vendaId;

	@PathParam("lancheId")
	private Integer lancheId;

	@POST
	public Venda addIngrediente(final Ingrediente ingrediente) {
		final Venda venda = database.getVenda(vendaId);
		venda.addIngrediente(lancheId, ingrediente);
		return venda;
	}

	@POST
	@Path("{id}/remove")
	public Venda removeIngrediente(@PathParam("id") final Integer ingredienteId) {
		final Venda venda = database.getVenda(vendaId);
		final Ingrediente ingrediente = database.getIngrediente(ingredienteId);
		venda.removeIngredienteTotal(lancheId, ingrediente);
		return venda;
	}

	@POST
	@Path("{id}/subtract")
	public Venda subtractIngrediente(@PathParam("id") final Integer ingredienteId) {
		final Venda venda = database.getVenda(vendaId);
		final Ingrediente ingrediente = database.getIngrediente(ingredienteId);
		venda.subtractIngrediente(lancheId, ingrediente);
		return venda;
	}
}
