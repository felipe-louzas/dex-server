package br.com.dextra.dex.server.restricoes;

public class DefaultComparacao<T extends Comparable<T>> implements Comparacao<T> {

	private final T a;
	private final T b;

	public DefaultComparacao(final T a, final T b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int getResult() {
		return a.compareTo(b);
	}
}
