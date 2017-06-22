package br.com.dextra.dex.server.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.db.sample.SampleDbData;
import br.com.dextra.dex.server.db.sample.SimpleMemoryDb;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.Lanche;
import br.com.dextra.dex.server.domain.LancheConfig;

public class LancheTests {

	private Database database;

	@Before
	public void setUp() {
		database = new SimpleMemoryDb();

		final SampleDbData sampleData = new SampleDbData();
		sampleData.load(database);
	}

	@After
	public void tearDown() {
		database.dropAll();
	}

	@Test
	public void testValorXBacon() {
		testeValorLanche("X-Bacon", "Bacon", "Hambúrguer de carne", "Queijo");
	}

	@Test
	public void testValorXBurger() {
		testeValorLanche("X-Burger", "Hambúrguer de carne", "Queijo");
	}

	@Test
	public void testValorXEgg() {
		testeValorLanche("X-Egg", "Ovo", "Hambúrguer de carne", "Queijo");
	}

	@Test
	public void testValorXEggBacon() {
		testeValorLanche("X-Egg Bacon", "Ovo", "Bacon", "Hambúrguer de carne", "Queijo");
	}

	@Test
	public void testValorXTudo() {
		final BigDecimal valorIngredientes = getValorIngredientes("Alface", "Ovo", "Bacon", "Hambúrguer de carne", "Queijo");

		final LancheConfig xEggBaconConfig = database.findLancheConfig((p) -> "X-Egg Bacon".equals(p.getDescricao()));
		final Lanche xEggBacon = xEggBaconConfig.getLanche();

		final Ingrediente alface = database.findIngrediente(p -> "Alface".equals(p.getDescricao()));

		xEggBacon.addIngrediente(alface);
		assertThat(xEggBacon.getValorBruto(), is(equalTo(valorIngredientes)));
	}

	@Test
	public void testValorXSalada() {
		final BigDecimal valorIngredientes = getValorIngredientes("Alface", "Hambúrguer de carne");

		final LancheConfig xBurgerConfig = database.findLancheConfig((p) -> "X-Burger".equals(p.getDescricao()));
		final Lanche xBurger = xBurgerConfig.getLanche();

		final Ingrediente alface = database.findIngrediente(p -> "Alface".equals(p.getDescricao()));
		final Ingrediente queijo = database.findIngrediente(p -> "Queijo".equals(p.getDescricao()));

		xBurger.addIngrediente(alface);
		xBurger.removeIngrediente(queijo);

		assertThat(xBurger.getValorBruto(), is(equalTo(valorIngredientes)));
	}

	@Test
	public void testValorRemoveNaoExistente() {
		final BigDecimal valorIngredientes = getValorIngredientes("Alface", "Hambúrguer de carne");

		final LancheConfig xBurgerConfig = database.findLancheConfig((p) -> "X-Burger".equals(p.getDescricao()));
		final Lanche xBurger = xBurgerConfig.getLanche();

		final Ingrediente alface = database.findIngrediente(p -> "Alface".equals(p.getDescricao()));
		final Ingrediente queijo = database.findIngrediente(p -> "Queijo".equals(p.getDescricao()));
		final Ingrediente ovo = database.findIngrediente(p -> "Ovo".equals(p.getDescricao()));

		xBurger.addIngrediente(alface);
		xBurger.removeIngrediente(queijo);
		xBurger.removeIngrediente(queijo);
		xBurger.removeIngrediente(ovo);

		assertThat(xBurger.getValorBruto(), is(equalTo(valorIngredientes)));
	}

	private void testeValorLanche(final String nomeLanche, final String... nomeIngredientes) {
		final BigDecimal valorIngredientes = getValorIngredientes(nomeIngredientes);

		final LancheConfig lancheConfig = database.findLancheConfig(p -> nomeLanche.equals(p.getDescricao()));
		final Lanche lanche = lancheConfig.getLanche();

		assertThat(lanche.getValorBruto(), is(equalTo(valorIngredientes)));
	}

	private BigDecimal getValorIngredientes(final String... nomeIngredientes) {
		BigDecimal valorIngredientes = BigDecimal.ZERO;
		for (final String nomeIngrediente : nomeIngredientes) {
			final Ingrediente ingrediente = database.findIngrediente(p -> nomeIngrediente.equals(p.getDescricao()));
			valorIngredientes = valorIngredientes.add(ingrediente.getValor());
		}

		return valorIngredientes;
	}
}
