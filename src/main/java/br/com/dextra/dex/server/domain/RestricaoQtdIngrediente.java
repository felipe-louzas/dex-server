package br.com.dextra.dex.server.domain;

import br.com.dextra.dex.server.restricoes.Comparacao;
import br.com.dextra.dex.server.restricoes.DefaultComparacao;
import br.com.dextra.dex.server.restricoes.Restricao;
import br.com.dextra.dex.server.restricoes.TipoComparacao;

public class RestricaoQtdIngrediente extends Entidade implements Restricao<Lanche> {

	private Ingrediente ingrediente;
	private TipoComparacao tipoComparacao;
	private Integer quantidade;

	public RestricaoQtdIngrediente() {
		// Construtor vazio
	}

	public RestricaoQtdIngrediente(final Ingrediente ingrediente, final TipoComparacao tipoComparacao, final Integer quantidade) {
		this.ingrediente = ingrediente;
		this.tipoComparacao = tipoComparacao;
		this.quantidade = quantidade;
	}

	@Override
	public boolean test(final Lanche lanche) {
		final Integer ingredienteCount = lanche.getIngredienteCount(ingrediente);
		final Comparacao<Integer> comparacao = new DefaultComparacao<>(ingredienteCount, quantidade);
		return tipoComparacao.test(comparacao);
	}

}
