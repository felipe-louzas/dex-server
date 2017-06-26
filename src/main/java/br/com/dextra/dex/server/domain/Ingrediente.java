package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;

public class Ingrediente extends Entidade implements CatalogoItem {

	private String descricao;
	private BigDecimal valor;

	public Ingrediente() {
		// Construtor vazio
	}

	public Ingrediente(final String descricao, final String valor) {
		this(descricao, new BigDecimal(valor));
	}

	public Ingrediente(final String descricao, final BigDecimal valor) {
		super();
		this.descricao = descricao;
		this.valor = valor;
	}

	public Ingrediente(final Ingrediente ingrediente) {
		super(ingrediente);
		this.descricao = ingrediente.descricao;
		this.valor = ingrediente.valor;
	}

	@Override
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Override
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

}
