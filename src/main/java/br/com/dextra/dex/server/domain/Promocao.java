package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;

public abstract class Promocao extends Entidade {
	private String descricao;

	public Promocao() {
		// Construtor vazio
	}

	public Promocao(final String descricao) {
		this.descricao = descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public boolean applyTo(final Lanche lanche) {
		if (!appliesTo(lanche)) return false;
		lanche.addPromocao(this, getValorDesconto(lanche));
		return true;
	}

	public abstract boolean appliesTo(Lanche lanche);

	public abstract BigDecimal getValorDesconto(Lanche lanche);

}
