package br.com.dextra.dex.server.db;

import java.util.Collection;
import java.util.function.Predicate;

import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.Promocao;
import br.com.dextra.dex.server.domain.Venda;

public interface Database {

	public enum Tipo {
		INGREDIENTE,
		LANCHE_CONFIG,
		PROMOCAO,
		VENDA
	}

	void saveIngrediente(Ingrediente ingrediente);

	void saveLancheConfig(LancheConfig lancheConfig);

	void savePromocao(Promocao promocao);

	void saveVenda(Venda venda);

	Ingrediente getIngrediente(Integer id);

	LancheConfig getLancheConfig(Integer id);

	Promocao getPromocao(Integer id);

	Venda getVenda(Integer id);

	Collection<Ingrediente> getIngredientes();

	Collection<LancheConfig> getLancheConfigs();

	Collection<Promocao> getPromocoes();

	Collection<Venda> getVendas();

	public Ingrediente findIngrediente(final Predicate<Ingrediente> predicate);

	public LancheConfig findLancheConfig(final Predicate<LancheConfig> predicate);

	public Promocao findPromocao(final Predicate<Promocao> predicate);

	public Venda findVenda(final Predicate<Venda> predicate);

	void dropAll();

}
