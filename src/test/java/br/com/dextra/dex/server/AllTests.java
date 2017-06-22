package br.com.dextra.dex.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.dextra.dex.server.domain.LancheTests;
import br.com.dextra.dex.server.domain.PromocoesTest;

@RunWith(Suite.class)
@SuiteClasses({ DbTests.class, SampleDataTests.class, LancheTests.class, PromocoesTest.class, VendaTests.class })
public class AllTests {
	// Vazia
}
