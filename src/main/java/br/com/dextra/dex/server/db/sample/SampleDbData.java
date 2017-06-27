package br.com.dextra.dex.server.db.sample;

import java.math.BigDecimal;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.PromocaoGenerica;
import br.com.dextra.dex.server.domain.PromocaoLevePague;
import br.com.dextra.dex.server.domain.RestricaoQtdIngrediente;
import br.com.dextra.dex.server.domain.support.Desconto;
import br.com.dextra.dex.server.domain.support.TipoDesconto;
import br.com.dextra.dex.server.restricoes.TipoComparacao;

public class SampleDbData {

	public void load(final Database db) {
		final Ingrediente alface = new Ingrediente("Alface", "0.40");
		final Ingrediente bacon = new Ingrediente("Bacon", "2.00");
		final Ingrediente burger = new Ingrediente("Hambúrguer de carne", "3.00");
		final Ingrediente ovo = new Ingrediente("Ovo", "0.80");
		final Ingrediente queijo = new Ingrediente("Queijo", "1.50");

		final LancheConfig lanchePersonalizado = new LancheConfig("Lanche");
		final LancheConfig xBacon = new LancheConfig("X-Bacon", bacon, burger, queijo);
		final LancheConfig xBurger = new LancheConfig("X-Burger", burger, queijo);
		final LancheConfig xEgg = new LancheConfig("X-Egg", ovo, burger, queijo);
		final LancheConfig xEggBacon = new LancheConfig("X-Egg Bacon", ovo, bacon, burger, queijo);

		final PromocaoGenerica promoLight = new PromocaoGenerica("Promoção Light");
		promoLight.setDesconto(new Desconto(TipoDesconto.PORCENTAGEM, BigDecimal.TEN));
		promoLight.addRestricao(new RestricaoQtdIngrediente(alface, TipoComparacao.MAIOR_QUE, 0));
		promoLight.addRestricao(new RestricaoQtdIngrediente(bacon, TipoComparacao.IGUAL, 0));

		final PromocaoLevePague promoCarne = new PromocaoLevePague("Promoção Muita Carne");
		promoCarne.setLeveQtd(3);
		promoCarne.setPagueQtd(2);
		promoCarne.setIngredientes(bacon, burger);

		final PromocaoLevePague promoQueijo = new PromocaoLevePague("Promoção Muito Queijo");
		promoQueijo.setLeveQtd(3);
		promoQueijo.setPagueQtd(2);
		promoQueijo.setIngredientes(queijo);

		db.saveIngrediente(alface);
		db.saveIngrediente(bacon);
		db.saveIngrediente(burger);
		db.saveIngrediente(ovo);
		db.saveIngrediente(queijo);

		db.saveLancheConfig(lanchePersonalizado);
		db.saveLancheConfig(xBacon);
		db.saveLancheConfig(xBurger);
		db.saveLancheConfig(xEgg);
		db.saveLancheConfig(xEggBacon);

		db.savePromocao(promoLight);
		db.savePromocao(promoCarne);
		db.savePromocao(promoQueijo);
	}

}
