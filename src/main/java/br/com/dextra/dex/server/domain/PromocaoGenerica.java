package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.dextra.dex.server.domain.support.Desconto;
import br.com.dextra.dex.server.restricoes.Restricao;

public class PromocaoGenerica extends Promocao {

	private Desconto desconto;
	private List<Restricao<Lanche>> restricoes;

	public PromocaoGenerica() {
		// Construtor vazio
	}

	public PromocaoGenerica(final String descricao) {
		super(descricao);
	}

	public void setDesconto(final Desconto desconto) {
		this.desconto = desconto;
	}

	public Desconto getDesconto() {
		return this.desconto;
	}

	public List<Restricao<Lanche>> getRestricoes() {
		if (restricoes == null) {
			restricoes = new ArrayList<>();
		}
		return restricoes;
	}

	public void addRestricao(final Restricao<Lanche> restricao) {
		getRestricoes().add(restricao);
	}

	@Override
	public boolean appliesTo(final Lanche lanche) {
		if (restricoes == null) return true;

		for (final Restricao<Lanche> restricao : getRestricoes()) {
			final boolean pass = restricao.test(lanche);
			if (!pass) return false;
		}

		return true;
	}

	@Override
	public BigDecimal getValorDesconto(final Lanche lanche) {
		if (!appliesTo(lanche)) return BigDecimal.ZERO;

		switch (desconto.getTipoDesconto()) {
			case PORCENTAGEM:
				return lanche.getValorBruto().multiply(desconto.getValor()).movePointLeft(2).setScale(2, RoundingMode.HALF_EVEN);

			case VALOR:
				return desconto.getValor().setScale(2, RoundingMode.HALF_EVEN);
		}

		return BigDecimal.ZERO;
	}

}
