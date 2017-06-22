/**
 *
 */
package br.com.dextra.dex.server;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.db.sample.SampleDbData;
import br.com.dextra.dex.server.db.sample.SimpleMemoryDb;

/**
 * @author Felipe
 */
public class SampleDataTests {

	private Database database;

	@Before
	public void setUp() {
		this.database = new SimpleMemoryDb();

		final SampleDbData sampleData = new SampleDbData();
		sampleData.load(database);
	}

	@After
	public void tearDown() {
		database.dropAll();
	}

	@Test
	public void testSampleIngredientes() {
		final List<String> expected = Arrays.asList("Alface", "Bacon", "Hambúrguer de carne", "Ovo", "Queijo");
		final List<String> actual = database.getIngredientes().stream().map(o -> o.getDescricao()).collect(Collectors.toList());

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void testSampleLancheConfigs() {
		final List<String> expected = Arrays.asList("X-Bacon", "X-Burger", "X-Egg", "X-Egg Bacon");
		final List<String> actual = database.getLancheConfigs().stream().map(o -> o.getDescricao()).collect(Collectors.toList());

		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void testSamplePromocoes() {
		final List<String> expected = Arrays.asList("Promoção Light", "Promoção Muita Carne", "Promoção Muito Queijo");
		final List<String> actual = database.getPromocoes().stream().map(o -> o.getDescricao()).collect(Collectors.toList());

		assertThat(actual, is(equalTo(expected)));
	}
}
