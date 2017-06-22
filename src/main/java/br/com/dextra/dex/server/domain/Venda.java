package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
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

	public Integer addLanche() {
		return addLanche(new Lanche("Lanche Personalizado"));
	}

	public Integer addLanche(final LancheConfig config) {
		return addLanche(config.getLanche());
	}

	private Integer addLanche(final Lanche lanche) {
		lanches.values().removeIf(v -> v.getIngredientes().isEmpty());

		final Integer lancheId = idGenerator.incrementAndGet();
		lanches.put(lancheId, lanche);
		return lancheId;
	}

	public void addIngrediente(final Integer lancheId, final Ingrediente ingrediente) {
		final Lanche lanche = lanches.get(lancheId);
		if (lanche == null) return;

		lanche.addIngrediente(ingrediente);

		if (promoManager != null) promoManager.applyPromocoes(lanche);
	}

	public void removeIngrediente(final Integer lancheId, final Ingrediente ingrediente) {
		final Lanche lanche = lanches.get(lancheId);
		if (lanche == null) return;

		lanche.removeIngrediente(ingrediente);

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
