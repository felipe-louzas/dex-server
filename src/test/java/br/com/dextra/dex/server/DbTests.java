package br.com.dextra.dex.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.db.sample.SampleDbData;
import br.com.dextra.dex.server.db.sample.SimpleMemoryDb;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.Promocao;
import br.com.dextra.dex.server.domain.PromocaoGenerica;
import br.com.dextra.dex.server.domain.PromocaoLevePague;
import br.com.dextra.dex.server.domain.Venda;

public class DbTests {

	private Database database;

	@Before
	public void setUp() {
		database = new SimpleMemoryDb();
	}

	@After
	public void tearDown() {
		database.dropAll();
	}

	@Test
	public void testSaveAndGetIngrediente() {
		final Ingrediente ingrediente = new Ingrediente("Salada", "0.10");
		assertThat(ingrediente.getId(), is(nullValue()));

		database.saveIngrediente(ingrediente);
		assertThat(ingrediente.getId(), is(greaterThan(0)));

		final Ingrediente ingredienteById = database.getIngrediente(ingrediente.getId());
		assertThat(ingredienteById, is(equalTo(ingrediente)));

		final Ingrediente ingredienteByDescricao = database.findIngrediente((p) -> "Salada".equals(p.getDescricao()));
		assertThat(ingredienteByDescricao, is(equalTo(ingrediente)));
		assertThat(ingredienteByDescricao, is(not(sameInstance(ingrediente))));

		final Collection<Ingrediente> ingredientes = database.getIngredientes();
		assertThat(ingredientes, containsInAnyOrder(ingrediente));
	}

	@Test
	public void testSaveAndGetLancheConfig() {
		final LancheConfig config = new LancheConfig("X-Burger");
		assertThat(config.getId(), is(nullValue()));

		database.saveLancheConfig(config);
		assertThat(config.getId(), is(greaterThan(0)));

		final LancheConfig configById = database.getLancheConfig(config.getId());
		assertThat(configById, is(equalTo(config)));

		final LancheConfig configByDescricao = database.findLancheConfig((p) -> "X-Burger".equals(p.getDescricao()));
		assertThat(configByDescricao, is(equalTo(config)));
		assertThat(configByDescricao, is(not(sameInstance(config))));

		final Collection<LancheConfig> configs = database.getLancheConfigs();
		assertThat(configs, containsInAnyOrder(config));
	}

	@Test
	public void testSaveAndGetPromocao() {
		final PromocaoGenerica promoGenerica = new PromocaoGenerica("Promoção Genérica");
		final PromocaoLevePague promoLevePague = new PromocaoLevePague("Promoção Leve e Pague");
		assertThat(promoGenerica.getId(), is(nullValue()));
		assertThat(promoLevePague.getId(), is(nullValue()));

		database.savePromocao(promoGenerica);
		database.savePromocao(promoLevePague);
		assertThat(promoGenerica.getId(), is(greaterThan(0)));
		assertThat(promoLevePague.getId(), is(greaterThan(0)));

		final Promocao promoGenById = database.getPromocao(promoGenerica.getId());
		final Promocao promoLevePagueById = database.getPromocao(promoLevePague.getId());
		assertThat(promoGenById, is(equalTo(promoGenerica)));
		assertThat(promoLevePagueById, is(equalTo(promoLevePague)));

		final Promocao promoGenByDesc = database.findPromocao((p) -> "Promoção Genérica".equals(p.getDescricao()));
		final Promocao promoLevePagueByDesc = database.findPromocao((p) -> "Promoção Leve e Pague".equals(p.getDescricao()));
		assertThat(promoGenByDesc, is(equalTo(promoGenerica)));
		assertThat(promoLevePagueByDesc, is(equalTo(promoLevePague)));

		final Collection<Promocao> promocoes = database.getPromocoes();
		assertThat(promocoes, containsInAnyOrder(promoGenerica, promoLevePague));
	}

	@Test
	public void testSaveAndGetVenda() {
		final Venda venda = new Venda();
		assertThat(venda.getId(), is(nullValue()));

		database.saveVenda(venda);
		assertThat(venda.getId(), is(greaterThan(0)));

		final Venda vendaDb = database.getVenda(venda.getId());
		assertThat(vendaDb, is(equalTo(venda)));

		final Collection<Venda> vendas = database.getVendas();
		assertThat(vendas, containsInAnyOrder(venda));
	}

	@Test
	public void testDropAll() {
		final SampleDbData sample = new SampleDbData();
		sample.load(database);

		assertThat(database.getIngredientes().size(), is(greaterThan(0)));
		assertThat(database.getLancheConfigs().size(), is(greaterThan(0)));
		assertThat(database.getPromocoes().size(), is(greaterThan(0)));

		database.dropAll();

		assertThat(database.getIngredientes().size(), is(equalTo(0)));
		assertThat(database.getLancheConfigs().size(), is(equalTo(0)));
		assertThat(database.getPromocoes().size(), is(equalTo(0)));
		assertThat(database.getVendas().size(), is(equalTo(0)));
	}
}
