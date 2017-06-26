package br.com.dextra.dex.server.domain;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.db.sample.SampleDbData;
import br.com.dextra.dex.server.db.sample.SimpleMemoryDb;

public class PromocoesTest {

	private Database database;

	private Ingrediente bacon;
	private Ingrediente alface;
	private Ingrediente burger;
	private Ingrediente queijo;

	private Lanche xEgg;
	private Lanche xBacon;

	private Promocao promoLight;
	private Promocao promoMaisCarne;
	private Promocao promoMaisQueijo;

	@Before
	public void setUp() {
		database = new SimpleMemoryDb();

		final SampleDbData sampleData = new SampleDbData();
		sampleData.load(database);

		bacon = database.findIngrediente(p -> "Bacon".equals(p.getDescricao()));
		alface = database.findIngrediente(p -> "Alface".equals(p.getDescricao()));
		burger = database.findIngrediente(p -> "Hambúrguer de carne".equals(p.getDescricao()));
		queijo = database.findIngrediente(p -> "Queijo".equals(p.getDescricao()));

		promoLight = database.findPromocao(p -> "Promoção Light".equals(p.getDescricao()));
		promoMaisCarne = database.findPromocao(p -> "Promoção Muita Carne".equals(p.getDescricao()));
		promoMaisQueijo = database.findPromocao(p -> "Promoção Muito Queijo".equals(p.getDescricao()));

		final LancheConfig xEggConfig = database.findLancheConfig(p -> "X-Egg".equals(p.getDescricao()));
		final LancheConfig xBaconConfig = database.findLancheConfig(p -> "X-Bacon".equals(p.getDescricao()));

		xEgg = xEggConfig.buildLanche();
		xBacon = xBaconConfig.buildLanche();
	}

	@After
	public void tearDown() {
		database.dropAll();
	}

	@Test
	public void testPromocaoLightEmptyLanche() {
		final Lanche lanche = new Lanche();
		assertThat(promoLight.appliesTo(lanche), is(false));
	}

	@Test
	public void testPromocaoLightSoAlface() {
		final Lanche lanche = new Lanche();
		lanche.addIngrediente(alface);
		assertThat(promoLight.appliesTo(lanche), is(true));
	}

	@Test
	public void testPromocaoLightAlfaceBacon() {
		final Lanche lanche = new Lanche();
		lanche.addIngrediente(alface);
		assertThat(promoLight.appliesTo(lanche), is(true));

		lanche.addIngrediente(bacon);
		assertThat(promoLight.appliesTo(lanche), is(false));
	}

	@Test
	public void testPromocaoLightXEgg() {
		assertThat(promoLight.appliesTo(xEgg), is(false));
	}

	@Test
	public void testPromocaoLightXBacon() {
		assertThat(promoLight.appliesTo(xBacon), is(false));
	}

	@Test
	public void testPromocaoLightXBaconComAlface() {
		xBacon.addIngrediente(alface);

		assertThat(promoLight.appliesTo(xBacon), is(false));
	}

	@Test
	public void testPromocaoLightXEggComAlface() {
		xEgg.addIngrediente(alface);

		assertThat(promoLight.appliesTo(xEgg), is(true));
	}

	@Test
	public void testPromocaoLightRemoveBacon() {
		assertThat(promoLight.appliesTo(xBacon), is(false));

		xBacon.addIngrediente(alface);
		assertThat(promoLight.appliesTo(xBacon), is(false));

		xBacon.subtractIngrediente(bacon);
		assertThat(promoLight.appliesTo(xBacon), is(true));
	}

	@Test
	public void testPromocaoLightDoisBacons() {
		xBacon.addIngrediente(bacon);
		xBacon.addIngrediente(alface);
		xBacon.subtractIngrediente(bacon);

		assertThat(promoLight.appliesTo(xBacon), is(false));

		xBacon.subtractIngrediente(bacon);
		assertThat(promoLight.appliesTo(xBacon), is(true));
	}

	@Test
	public void testPromocaoLightValorXBaconComAlfaceSemBacon() {
		xBacon.addIngrediente(alface);
		xBacon.subtractIngrediente(bacon);

		final BigDecimal tenPercent = xBacon.getValorBruto().movePointLeft(1).setScale(2, RoundingMode.HALF_EVEN);

		assertThat(promoLight.appliesTo(xBacon), is(true));
		assertThat(promoLight.getValorDesconto(xBacon), is(equalTo(tenPercent)));
	}

	@Test
	public void testPromocaoLightValorXEggComAlface() {
		xEgg.addIngrediente(alface);

		final BigDecimal tenPercent = xEgg.getValorBruto().movePointLeft(1).setScale(2, RoundingMode.HALF_EVEN);

		assertThat(promoLight.appliesTo(xEgg), is(true));
		assertThat(promoLight.getValorDesconto(xEgg), is(equalTo(tenPercent)));
	}

	@Test
	public void testPromocaoLightValorSoAlface() {
		final Lanche lanche = new Lanche();
		lanche.addIngrediente(alface);

		final BigDecimal tenPercent = lanche.getValorBruto().movePointLeft(1).setScale(2, RoundingMode.HALF_EVEN);

		assertThat(promoLight.appliesTo(lanche), is(true));
		assertThat(promoLight.getValorDesconto(lanche), is(equalTo(tenPercent)));
	}

	@Test
	public void testPromocaoLightValorXBacon() {
		assertThat(promoLight.getValorDesconto(xBacon), is(equalTo(BigDecimal.ZERO)));
	}

	@Test
	public void testPromocaoMaisCarneXBacon() {
		assertThat(promoMaisCarne.appliesTo(xBacon), is(false));
		assertThat(promoMaisCarne.getValorDesconto(xBacon), is(equalTo(BigDecimal.ZERO)));
	}

	@Test
	public void testPromocaoMaisCarneXBaconBurger() {
		xBacon.addIngrediente(burger);

		final Ingrediente menorValor = burger.getValor().compareTo(bacon.getValor()) <= 0 ? burger : bacon;

		assertThat(promoMaisCarne.appliesTo(xBacon), is(true));
		assertThat(promoMaisCarne.getValorDesconto(xBacon), is(equalTo(menorValor.getValor())));
	}

	@Test
	public void testPromocaoMaisCarneXBaconMaisBurger() {
		xBacon.addIngrediente(burger);
		xBacon.addIngrediente(burger);

		final Ingrediente menorValor = burger.getValor().compareTo(bacon.getValor()) <= 0 ? burger : bacon;

		assertThat(promoMaisCarne.appliesTo(xBacon), is(true));
		assertThat(promoMaisCarne.getValorDesconto(xBacon), is(equalTo(menorValor.getValor())));
	}

	@Test
	public void testPromocaoMaisCarneMuitoBurger() {
		xBacon.addIngrediente(burger);
		xBacon.addIngrediente(burger);
		xBacon.addIngrediente(burger);
		xBacon.addIngrediente(burger);

		final BigDecimal valorDesconto = burger.getValor().compareTo(bacon.getValor()) <= 0 ? burger.getValor().multiply(new BigDecimal(2)) : bacon.getValor().add(burger.getValor());

		assertThat(promoMaisCarne.appliesTo(xBacon), is(true));
		assertThat(promoMaisCarne.getValorDesconto(xBacon), is(equalTo(valorDesconto)));
	}

	@Test
	public void testPromocaoMaisCarneMuitoBacon() {
		xBacon.addIngrediente(bacon);
		xBacon.addIngrediente(bacon);
		xBacon.addIngrediente(bacon);
		xBacon.addIngrediente(bacon);

		final BigDecimal valorDesconto = burger.getValor().compareTo(bacon.getValor()) <= 0 ? burger.getValor().add(bacon.getValor()) : bacon.getValor().multiply(new BigDecimal(2));

		assertThat(promoMaisCarne.appliesTo(xBacon), is(true));
		assertThat(promoMaisCarne.getValorDesconto(xBacon), is(equalTo(valorDesconto)));
	}

	@Test
	public void testPromocaoMaisQueijo2Queijo() {
		xBacon.addIngrediente(queijo);

		assertThat(promoMaisQueijo.appliesTo(xBacon), is(false));
		assertThat(promoMaisQueijo.getValorDesconto(xBacon), is(equalTo(BigDecimal.ZERO)));
	}

	@Test
	public void testPromocaoMaisQueijo3Queijo() {
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);

		assertThat(promoMaisQueijo.appliesTo(xBacon), is(true));
		assertThat(promoMaisQueijo.getValorDesconto(xBacon), is(equalTo(queijo.getValor())));
	}

	@Test
	public void testPromocaoMaisQueijo7Queijo() {
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);

		assertThat(promoMaisQueijo.appliesTo(xBacon), is(true));
		assertThat(promoMaisQueijo.getValorDesconto(xBacon), is(equalTo(queijo.getValor().multiply(new BigDecimal(2)))));
	}

	@Test
	public void testPromocaoMaisQueijoRemove() {
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo);
		xBacon.addIngrediente(queijo); // 7 Queijos

		assertThat(promoMaisQueijo.appliesTo(xBacon), is(true));
		assertThat(promoMaisQueijo.getValorDesconto(xBacon), is(equalTo(queijo.getValor().multiply(new BigDecimal(2)))));

		xBacon.subtractIngrediente(queijo);
		xBacon.subtractIngrediente(queijo); // 5 Queijos

		assertThat(promoMaisQueijo.appliesTo(xBacon), is(true));
		assertThat(promoMaisQueijo.getValorDesconto(xBacon), is(equalTo(queijo.getValor())));

		xBacon.subtractIngrediente(queijo);
		xBacon.subtractIngrediente(queijo);
		xBacon.subtractIngrediente(queijo); // 2 Queijos

		assertThat(promoMaisQueijo.appliesTo(xBacon), is(false));
		assertThat(promoMaisQueijo.getValorDesconto(xBacon), is(equalTo(BigDecimal.ZERO)));
	}

	@Test
	public void testPromocaoMaisCarneLancheVazio() {
		assertThat(promoMaisCarne.appliesTo(new Lanche()), is(false));
		assertThat(promoMaisCarne.getValorDesconto(new Lanche()), is(equalTo(BigDecimal.ZERO)));
	}
}
