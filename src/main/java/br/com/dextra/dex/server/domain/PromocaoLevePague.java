package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PromocaoLevePague extends Promocao {

	private int pagueQtd;
	private int leveQtd;
	private List<Ingrediente> ingredientes;

	public PromocaoLevePague() {
		// construtor vazio
	}

	public PromocaoLevePague(final String descricao) {
		super(descricao);
	}

	public int getPagueQtd() {
		return this.pagueQtd;
	}

	public void setPagueQtd(final int pagueQtd) {
		this.pagueQtd = pagueQtd;

	}

	public int getLeveQtd() {
		return this.leveQtd;
	}

	public void setLeveQtd(final int leveQtd) {
		this.leveQtd = leveQtd;

	}

	public void setIngredientes(final Ingrediente... ingredientes) {
		this.ingredientes = Arrays.asList(ingredientes);
	}

	@Override
	public boolean appliesTo(final Lanche lanche) {
		int totalCount = 0;
		for (final Ingrediente ingrediente : ingredientes) {
			totalCount += lanche.getIngredienteCount(ingrediente);

			if (totalCount >= getLeveQtd()) return true;
		}

		return false;
	}

	@Override
	public BigDecimal getValorDesconto(final Lanche lanche) {
		if (!appliesTo(lanche)) return BigDecimal.ZERO;

		final List<Ingrediente> matchedIngredientes = new ArrayList<>();

		for (final Ingrediente ingredienteLanche : lanche.getIngredientes()) {
			for (final Ingrediente ingredientePromocao : ingredientes) {
				if (ingredienteLanche.equals(ingredientePromocao)) matchedIngredientes.add(ingredienteLanche);
			}
		}

		Collections.sort(matchedIngredientes, (i1, i2) -> i1.getValor().compareTo(i2.getValor()));

		final int applyTimes = Math.floorDiv(matchedIngredientes.size(), leveQtd);

		BigDecimal valorDesconto = BigDecimal.ZERO;
		for (int i = 0; i < (leveQtd - pagueQtd) * applyTimes; i++) {
			valorDesconto = valorDesconto.add(matchedIngredientes.get(i).getValor());
		}

		return valorDesconto;
	}

}
