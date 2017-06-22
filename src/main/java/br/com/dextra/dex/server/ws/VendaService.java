package br.com.dextra.dex.server.ws;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.Lanche;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.Venda;
import br.com.dextra.dex.server.promo.DefaultPromocaoManager;
import br.com.dextra.dex.server.promo.PromocaoManager;

@Path("/venda/")
@Produces(MediaType.APPLICATION_JSON)
public class VendaService {

	@Inject
	private Database database;

	@GET
	@Path("nova")
	public Venda getNovaVenda() {
		final PromocaoManager promoManager = new DefaultPromocaoManager(database.getPromocoes());
		final Venda venda = new Venda(promoManager);
		database.saveVenda(venda);

		return venda;
	}

	@GET
	@Path("{id}")
	public Venda getVendaById(@PathParam("id") final Integer vendaId) {
		final Venda venda = database.getVenda(vendaId);
		return venda;
	}

	@GET
	@Path("{id}/lanches/novo")
	public Lanche novoLanche(@PathParam("id") final Integer vendaId) {
		final Venda venda = database.getVenda(vendaId);
		return venda.addLanche();
	}

	@GET
	@Path("{id}/lanches/novo/{tipo}")
	public Lanche novoLancheTipo(@PathParam("id") final Integer vendaId, @PathParam("tipo") final Integer configId) {
		final Venda venda = database.getVenda(vendaId);
		final LancheConfig config = database.getLancheConfig(configId);
		return venda.addLanche(config);
	}

	@GET
	@Path("{id}/lanches")
	public Collection<Lanche> getLanches(@PathParam("id") final Integer vendaId) {
		final Venda venda = database.getVenda(vendaId);
		return venda.getLanches();
	}

	@GET
	@Path("{id}/lanches/{num}")
	public Lanche getLanche(@PathParam("id") final Integer vendaId, @PathParam("num") final Integer lancheId) {
		final Venda venda = database.getVenda(vendaId);
		return venda.getLanche(lancheId);
	}

	@GET
	@Path("{id}/lanches/{num}/add/{ingred}")
	public Venda addIngrediente(@PathParam("id") final Integer vendaId, @PathParam("num") final Integer lancheId, @PathParam("ingred") final Integer ingredienteId) {
		final Venda venda = database.getVenda(vendaId);
		final Ingrediente ingrediente = database.getIngrediente(ingredienteId);
		venda.addIngrediente(lancheId, ingrediente);
		return venda;
	}

	@GET
	@Path("{id}/lanches/{num}/remove/{ingred}")
	public Venda removeIngrediente(@PathParam("id") final Integer vendaId, @PathParam("num") final Integer lancheId, @PathParam("ingred") final Integer ingredienteId) {
		final Venda venda = database.getVenda(vendaId);
		final Ingrediente ingrediente = database.getIngrediente(ingredienteId);
		venda.removeIngrediente(lancheId, ingrediente);
		return venda;
	}
}
