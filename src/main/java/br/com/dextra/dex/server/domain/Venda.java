package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.dextra.dex.server.promo.PromocaoManager;

public class Venda extends Entidade {

	private final AtomicInteger idGenerator = new AtomicInteger();
	private final Map<Integer, Lanche> lanches = new ConcurrentHashMap<>();
	private PromocaoManager promoManager;

	public Venda() {

	}

	public Venda(final PromocaoManager promoManager) {
		this.promoManager = promoManager;
	}

	public Lanche addLanche() {
		return addLanche(new Lanche("Lanche Personalizado"));
	}

	public Lanche addLanche(final LancheConfig config) {
		return addLanche(config.getLanche());
	}

	private Lanche addLanche(final Lanche lanche) {
		lanches.values().removeIf(v -> v.getIngredientes().isEmpty());

		final Integer lancheId = idGenerator.incrementAndGet();
		lanches.put(lancheId, lanche);
		lanche.setId(lancheId);
		return lanche;
	}

	public Collection<Lanche> getLanches() {
		final List<Lanche> lanchesOrdered = new ArrayList<>();
		lanchesOrdered.addAll(lanches.values());
		Collections.sort(lanchesOrdered, (a, b) -> a.getId().compareTo(b.getId()));
		return lanchesOrdered;
	}

	public Lanche getLanche(final Integer lancheId) {
		return lanches.get(lancheId);
	}

	public void addIngrediente(final Integer lancheId, final Ingrediente ingrediente) {
		final Lanche lanche = lanches.get(lancheId);
		if (lanche == null) return;

		lanche.addIngrediente(ingrediente);

		if (promoManager != null) promoManager.applyPromocoes(lanche);
	}

	public void removeIngredienteTotal(final Integer lancheId, final Ingrediente ingrediente) {
		final Lanche lanche = lanches.get(lancheId);
		if (lanche == null) return;

		lanche.removeIngredienteTotal(ingrediente);

		if (lanche.getIngredientes().isEmpty()) {
			lanches.remove(lancheId);
			return;
		}

		if (promoManager != null) promoManager.applyPromocoes(lanche);
	}

	public void subtractIngrediente(final Integer lancheId, final Ingrediente ingrediente) {
		final Lanche lanche = lanches.get(lancheId);
		if (lanche == null) return;

		lanche.subtractIngrediente(ingrediente);

		if (lanche.getIngredientes().isEmpty()) {
			lanches.remove(lancheId);
			return;
		}

		if (promoManager != null) promoManager.applyPromocoes(lanche);
	}

	public void removeLanche(final Integer lancheId) {
		lanches.remove(lancheId);
	}

	public int getLancheCount() {
		return lanches.size();
	}

	public BigDecimal getValorTotal() {
		return lanches.values().stream().map(Lanche::getValorComDescontos).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
	}

	public BigDecimal getValorBruto() {
		return lanches.values().stream().map(Lanche::getValorBruto).reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
	}

}
