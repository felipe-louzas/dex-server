package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lanche extends Entidade {

	private String descricao;
	private List<Ingrediente> ingredientes = new ArrayList<>();
	private final Map<Promocao, BigDecimal> promocoes = new HashMap<>();

	public Lanche() {
		// construtor vazio
	}

	public Lanche(final String descricao) {
		this.descricao = descricao;
		this.ingredientes = new ArrayList<>(ingredientes);
	}

	public Lanche(final String descricao, final List<Ingrediente> ingredientes) {
		this.descricao = descricao;
		this.ingredientes = new ArrayList<>(ingredientes);
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Collection<Ingrediente> getIngredientes() {
		return Collections.unmodifiableCollection(ingredientes);
	}

	public void addIngrediente(final Ingrediente ingrediente) {
		ingredientes.add(ingrediente);
	}

	public void removeIngrediente(final Ingrediente ingrediente) {
		ingredientes.remove(ingrediente);
	}

	public void addPromocao(final Promocao promocao, final BigDecimal valorDesconto) {
		promocoes.put(promocao, valorDesconto);
	}

	public void removePromocao(final Promocao promocao) {
		promocoes.remove(promocao);
	}

	public void clearPromocoes() {
		promocoes.clear();
	}

	public BigDecimal getValorComDescontos() {
		BigDecimal valor = getValorBruto();
		for (final BigDecimal desconto : promocoes.values()) {
			valor = valor.subtract(desconto);
		}
		return valor;
	}

	public BigDecimal getValorBruto() {
		BigDecimal valor = BigDecimal.ZERO;
		for (final Ingrediente ingrediente : ingredientes) {
			valor = valor.add(ingrediente.getValor());
		}
		return valor;
	}

	public Integer getIngredienteCount(final Ingrediente tipoIngrediente) {
		return ingredientes.stream().filter(p -> p.equals(tipoIngrediente)).collect(Collectors.counting()).intValue();
	}
}
