package br.com.dextra.dex.server.domain.support;

import java.math.BigDecimal;

public class Desconto {

	private TipoDesconto tipoDesconto;
	private BigDecimal valor;

	public Desconto() {
		// Construtor vazio
	}

	public Desconto(final TipoDesconto tipoDesconto, final BigDecimal valor) {
		this.tipoDesconto = tipoDesconto;
		this.valor = valor;
	}

	public TipoDesconto getTipoDesconto() {
		return this.tipoDesconto;
	}

	public void setTipoDesconto(final TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

}
