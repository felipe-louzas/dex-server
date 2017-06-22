package br.com.dextra.dex.server.restricoes;

@FunctionalInterface
public interface Restricao<T> {
	boolean test(T input);
}