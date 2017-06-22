package br.com.dextra.dex.server.promo;

import br.com.dextra.dex.server.domain.Lanche;

public interface PromocaoManager {
	void applyPromocoes(Lanche lanche);
}
