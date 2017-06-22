package br.com.dextra.dex.server.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LancheConfig extends Entidade {

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

	public String getDescricao() {
		return this.descricao;
	}

	public List<Ingrediente> getIngredientes() {
		if (ingredientes == null) {
			ingredientes = Collections.emptyList();
		}
		return this.ingredientes;
	}

	public Lanche getLanche() {
		return new Lanche(descricao, ingredientes);
	}

}
