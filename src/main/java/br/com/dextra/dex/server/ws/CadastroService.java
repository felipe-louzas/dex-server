package br.com.dextra.dex.server.ws;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.CatalogoItem;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;

@Path("/catalogo")
@Produces(MediaType.APPLICATION_JSON)
public class CadastroService {

	@Inject
	private Database database;

	@GET
	public Map<String, Collection<CatalogoItem>> getCatalogo() {
		final Map<String, Collection<CatalogoItem>> map = new HashMap<>();
		map.put("ingredientes", Collections.unmodifiableCollection(database.getIngredientes()));
		map.put("lanches", Collections.unmodifiableCollection(database.getLancheConfigs()));

		return map;
	}

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
