package br.com.dextra.dex.server.ws;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;

@Path("/cadastros/")
@Produces(MediaType.APPLICATION_JSON)
public class CadastroService {

	@Inject
	private Database database;

	@GET
	@Path("ingredientes")
	public Collection<Ingrediente> getIngredientes() {
		return database.getIngredientes();
	}

	@GET
	@Path("lanches")
	public Collection<LancheConfig> getLanches() {
		return database.getLancheConfigs();
	}
}
