package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LancheConfig extends Entidade implements CatalogoItem {

	private String descricao;
	private List<Ingrediente> ingredientes;

	public LancheConfig() {
		// Construtor vazio
	}

	public LancheConfig(final String descricao, final Ingrediente... ingredientes) {
		super();
		this.descricao = descricao;
		this.ingredientes = Arrays.asList(ingredientes);
	}

	public LancheConfig(final LancheConfig lancheConfig) {
		super(lancheConfig);
		this.descricao = lancheConfig.descricao;
		this.ingredientes = lancheConfig.ingredientes.stream().map(p -> new Ingrediente(p)).collect(Collectors.toList());
	}

	@Override
	public String getDescricao() {
		return this.descricao;
	}

	public List<Ingrediente> getIngredientes() {
		if (ingredientes == null) {
			ingredientes = Collections.emptyList();
		}
		return this.ingredientes;
	}

	public Lanche buildLanche() {
		return new Lanche(descricao, ingredientes);
	}

	@Override
	public BigDecimal getValor() {
		return ingredientes.stream().map(Ingrediente::getValor).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
	}

}
