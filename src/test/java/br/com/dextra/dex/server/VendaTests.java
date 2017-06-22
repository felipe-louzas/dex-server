package br.com.dextra.dex.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.db.sample.SampleDbData;
import br.com.dextra.dex.server.db.sample.SimpleMemoryDb;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.Venda;
import br.com.dextra.dex.server.promo.DefaultPromocaoManager;
import br.com.dextra.dex.server.promo.PromocaoManager;

public class VendaTests {

	private Database database;

	private Venda venda;

	private Ingrediente bacon;
	private Ingrediente alface;
	private Ingrediente burger;
	private Ingrediente queijo;
	private Ingrediente ovo;

	@Before
	public void setUp() {
		this.database = new SimpleMemoryDb();

		final SampleDbData sampleData = new SampleDbData();
		sampleData.load(database);

		final PromocaoManager defaultManager = new DefaultPromocaoManager(database.getPromocoes());

		venda = new Venda(defaultManager);

		bacon = database.findIngrediente(p -> "Bacon".equals(p.getDescricao()));
		alface = database.findIngrediente(p -> "Alface".equals(p.getDescricao()));
		burger = database.findIngrediente(p -> "Hambúrguer de carne".equals(p.getDescricao()));
		queijo = database.findIngrediente(p -> "Queijo".equals(p.getDescricao()));
		ovo = database.findIngrediente(p -> "Ovo".equals(p.getDescricao()));
	}

	@After
	public void tearDown() {
		database.dropAll();
	}

	@Test
	public void testVendaMaximoUmLancheVazio() {
		venda.addLanche();
		assertThat(venda.getLancheCount(), is(equalTo(1)));

		venda.addLanche();
		assertThat(venda.getLancheCount(), is(equalTo(1)));
	}

	@Test
	public void testVendaLancheVazioRemovido() {
		venda.addLanche();
		assertThat(venda.getLancheCount(), is(equalTo(1)));

		final BigDecimal valorXBurger = burger.getValor().add(queijo.getValor());

		venda.addLanche(getLanche("X-Burger"));
		assertThat(venda.getLancheCount(), is(equalTo(1)));
		assertThat(venda.getValorTotal(), is(equalTo(valorXBurger)));
	}

	@Test
	public void testVendaRemoveLancheAoRemoverUltimoIngrediente() {
		final Integer lancheId = venda.addLanche();
		assertThat(venda.getLancheCount(), is(equalTo(1)));

		venda.addIngrediente(lancheId, ovo);
		assertThat(venda.getValorTotal(), is(equalTo(ovo.getValor())));

		venda.removeIngrediente(lancheId, ovo);
		assertThat(venda.getLancheCount(), is(equalTo(0)));
		assertThat(venda.getValorTotal(), is(equalTo(BigDecimal.ZERO)));
	}

	@Test
	public void testVendaPromocaoLight() {
		final BigDecimal valorXEggSalada = soma(ovo, burger, queijo, alface).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_EVEN);
		final BigDecimal valorXEgg = soma(ovo, burger, queijo);

		final Integer lancheId = venda.addLanche(getLanche("X-Egg"));
		assertThat(venda.getLancheCount(), is(equalTo(1)));

		venda.addIngrediente(lancheId, alface);
		assertThat(venda.getValorTotal(), is(equalTo(valorXEggSalada)));

		venda.removeIngrediente(lancheId, alface);
		assertThat(venda.getLancheCount(), is(equalTo(1)));
		assertThat(venda.getValorTotal(), is(equalTo(valorXEgg)));
	}

	@Test
	public void testVendaPromocaoLightAddBacon() {
		final BigDecimal valorXEggSalada = soma(ovo, burger, queijo, alface).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_EVEN);
		final BigDecimal valorXEggSaladaBacon = soma(ovo, burger, queijo, alface, bacon);

		final Integer lancheId = venda.addLanche(getLanche("X-Egg"));
		assertThat(venda.getLancheCount(), is(equalTo(1)));

		venda.addIngrediente(lancheId, alface);
		assertThat(venda.getValorTotal(), is(equalTo(valorXEggSalada)));

		venda.addIngrediente(lancheId, bacon);
		assertThat(venda.getLancheCount(), is(equalTo(1)));
		assertThat(venda.getValorTotal(), is(equalTo(valorXEggSaladaBacon)));
	}

	@Test
	public void testVendaPromocaoMuitaCarneQueijo() {
		final BigDecimal valorXMuito = soma(ovo, burger, queijo, queijo, bacon, queijo, bacon, alface);
		final BigDecimal valorXMuitoDesconto = soma(ovo, burger, queijo, queijo, bacon, /* queijo, bacon, */ alface);

		final Integer lancheId = venda.addLanche(getLanche("X-Egg"));
		venda.addIngrediente(lancheId, queijo);
		venda.addIngrediente(lancheId, bacon);
		venda.addIngrediente(lancheId, queijo);
		venda.addIngrediente(lancheId, bacon);
		venda.addIngrediente(lancheId, alface);

		assertThat(venda.getValorTotal(), is(equalTo(valorXMuitoDesconto)));
		assertThat(venda.getValorBruto(), is(equalTo(valorXMuito)));

		venda.removeIngrediente(lancheId, bacon);
		assertThat(venda.getValorTotal(), is(equalTo(valorXMuitoDesconto)));
	}

	@Test
	public void testVendaPromocaoMultiLanche() {
		final BigDecimal valorTotal = soma(ovo, burger, queijo, bacon).multiply(new BigDecimal(4));

		venda.addLanche(getLanche("X-Egg Bacon"));
		venda.addLanche(getLanche("X-Egg Bacon"));
		venda.addLanche(getLanche("X-Egg Bacon"));
		final Integer lancheId = venda.addLanche(getLanche("X-Egg Bacon"));

		assertThat(venda.getLancheCount(), is(equalTo(4)));
		assertThat(venda.getValorTotal(), is(equalTo(valorTotal)));
		assertThat(venda.getValorBruto(), is(equalTo(valorTotal)));

		venda.addIngrediente(lancheId, bacon);

		assertThat(venda.getValorTotal(), is(equalTo(valorTotal)));
		assertThat(venda.getValorBruto(), is(equalTo(valorTotal.add(bacon.getValor()))));
	}

	private BigDecimal soma(final Ingrediente... ingredientes) {
		return Arrays.stream(ingredientes).map(Ingrediente::getValor).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
	}

	private LancheConfig getLanche(final String nomeLanche) {
		return database.findLancheConfig(p -> nomeLanche.equals(p.getDescricao()));
	}

}
