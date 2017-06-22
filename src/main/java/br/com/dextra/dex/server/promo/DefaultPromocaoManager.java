package br.com.dextra.dex.server.promo;

import java.util.Collection;

import br.com.dextra.dex.server.domain.Lanche;
import br.com.dextra.dex.server.domain.Promocao;

public class DefaultPromocaoManager implements PromocaoManager {

	private final Collection<Promocao> promocoes;

	public DefaultPromocaoManager(final Collection<Promocao> promocoes) {
		this.promocoes = promocoes;
	}

	@Override
	public void applyPromocoes(final Lanche lanche) {
		lanche.clearPromocoes();
		promocoes.forEach(p -> p.applyTo(lanche));
	}

}
