package br.com.dextra.dex.server.db.sample;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

import br.com.dextra.dex.server.db.Database;
import br.com.dextra.dex.server.domain.Entidade;
import br.com.dextra.dex.server.domain.Ingrediente;
import br.com.dextra.dex.server.domain.LancheConfig;
import br.com.dextra.dex.server.domain.Promocao;
import br.com.dextra.dex.server.domain.Venda;

/**
 * Implementação simples de {@code Database} para testes. <b>NÃO USAR EM PRODUÇÃO!</b>
 *
 * @author Felipe
 */
public class SimpleMemoryDb implements Database {

	private final AtomicInteger idGenerator = new AtomicInteger();
	private final ReadWriteLock dbLock = new ReentrantReadWriteLock();

	private final Map<Integer, Ingrediente> ingredientes = new HashMap<>();
	private final Map<Integer, LancheConfig> lancheConfigs = new HashMap<>();
	private final Map<Integer, Promocao> promocoes = new HashMap<>();
	private final Map<Integer, Venda> vendas = new HashMap<>();

	@Override
	public void saveIngrediente(final Ingrediente ingrediente) {
		persist(ingrediente, ingredientes);
	}

	@Override
	public void saveLancheConfig(final LancheConfig lancheConfig) {
		persist(lancheConfig, lancheConfigs);
	}

	@Override
	public void savePromocao(final Promocao promocao) {
		persist(promocao, promocoes);
	}

	@Override
	public void saveVenda(final Venda venda) {
		persist(venda, vendas);
	}

	@Override
	public Ingrediente getIngrediente(final Integer id) {
		return new Ingrediente(ingredientes.get(id));
	}

	@Override
	public LancheConfig getLancheConfig(final Integer id) {
		return new LancheConfig(lancheConfigs.get(id));
	}

	@Override
	public Promocao getPromocao(final Integer id) {
		return promocoes.get(id);
	}

	@Override
	public Venda getVenda(final Integer id) {
		return vendas.get(id);
	}

	@Override
	public synchronized void dropAll() {
		try {
			dbLock.writeLock().lock();
			idGenerator.set(0);
			ingredientes.clear();
			lancheConfigs.clear();
			promocoes.clear();
			vendas.clear();
		} finally {
			dbLock.writeLock().unlock();
		}
	}

	private <T extends Entidade> void persist(final T entidade, final Map<Integer, T> map) {
		try {
			dbLock.readLock().lock();
			if (entidade.getId() == null) {
				entidade.setId(idGenerator.incrementAndGet());
			}
			map.put(entidade.getId(), entidade);

		} finally {
			dbLock.readLock().unlock();
		}
	}

	@Override
	public Collection<Ingrediente> getIngredientes() {
		return Collections.unmodifiableCollection(ingredientes.values());
	}

	@Override
	public Collection<LancheConfig> getLancheConfigs() {
		return Collections.unmodifiableCollection(lancheConfigs.values());
	}

	@Override
	public Collection<Promocao> getPromocoes() {
		return Collections.unmodifiableCollection(promocoes.values());
	}

	@Override
	public Collection<Venda> getVendas() {
		return Collections.unmodifiableCollection(vendas.values());
	}

	@Override
	public Ingrediente findIngrediente(final Predicate<Ingrediente> predicate) {
		return new Ingrediente(find(predicate, ingredientes));
	}

	@Override
	public LancheConfig findLancheConfig(final Predicate<LancheConfig> predicate) {
		return new LancheConfig(find(predicate, lancheConfigs));

	}

	@Override
	public Promocao findPromocao(final Predicate<Promocao> predicate) {
		return find(predicate, promocoes);
	}

	@Override
	public Venda findVenda(final Predicate<Venda> predicate) {
		return find(predicate, vendas);
	}

	private <T> T find(final Predicate<T> predicate, final Map<Integer, T> map) {
		return map.values().stream().filter(predicate).findAny().orElse(null);
	}

}
