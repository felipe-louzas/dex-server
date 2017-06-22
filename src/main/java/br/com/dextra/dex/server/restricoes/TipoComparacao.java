package br.com.dextra.dex.server.restricoes;

public enum TipoComparacao implements Restricao<Comparacao<?>> {

	IGUAL(c -> c.getResult() == 0),
	DIFERENTE(c -> c.getResult() != 0),
	MAIOR_QUE(c -> c.getResult() > 0),
	MENOR_QUE(c -> c.getResult() < 0),
	MAIOR_OU_IGUAL(c -> c.getResult() >= 0),
	MENOR_OU_IGUAL(c -> c.getResult() <= 0);

	private final Restricao<Comparacao<?>> restricao;

	private TipoComparacao(final Restricao<Comparacao<?>> restricao) {
		this.restricao = restricao;
	}

	@Override
	public boolean test(final Comparacao<?> comparison) {
		return restricao.test(comparison);
	}

}
