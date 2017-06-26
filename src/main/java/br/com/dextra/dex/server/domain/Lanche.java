package br.com.dextra.dex.server.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Lanche extends Entidade {

	private String descricao;
	private final Map<Integer, QuantidadeIngrediente> ingredientes = new HashMap<>();
	private final Map<Promocao, BigDecimal> promocoes = new HashMap<>();

	public Lanche() {
		// construtor vazio
	}

	public Lanche(final String descricao) {
		this.descricao = descricao;
	}

	public Lanche(final String descricao, final List<Ingrediente> ingredientes) {
		this.descricao = descricao;
		ingredientes.forEach((p) -> addIngrediente(p));
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Collection<QuantidadeIngrediente> getIngredientes() {
		return Collections.unmodifiableCollection(ingredientes.values());
	}

	protected synchronized void addIngrediente(final Ingrediente ingrediente) {
		if (ingrediente.getId() == null) throw new IllegalArgumentException("Não pode adicionar ingrediente sem id");

		QuantidadeIngrediente ingredienteQtd = ingredientes.get(ingrediente.getId());
		if (ingredienteQtd == null) {
			ingredienteQtd = new QuantidadeIngrediente(ingrediente);
			ingredientes.put(ingrediente.getId(), ingredienteQtd);
		}
		ingredienteQtd.aumentaQuantidade(1);
	}

	protected void removeIngredienteTotal(final Ingrediente ingrediente) {
		if (ingrediente.getId() == null) throw new IllegalArgumentException("Não pode remover ingrediente sem id");

		ingredientes.remove(ingrediente.getId());
	}

	protected synchronized void subtractIngrediente(final Ingrediente ingrediente) {
		if (ingrediente.getId() == null) throw new IllegalArgumentException("Não pode subtrair ingrediente sem id");

		final QuantidadeIngrediente ingredienteQtd = ingredientes.get(ingrediente.getId());
		if (ingredienteQtd == null) {
			return;
		}

		if (ingredienteQtd.getQuantidade() <= 1) {
			removeIngredienteTotal(ingrediente);
			return;
		}

		ingredienteQtd.diminuiQuantidade(1);
	}

	protected void addPromocao(final Promocao promocao, final BigDecimal valorDesconto) {
		promocoes.put(promocao, valorDesconto);
	}

	protected void removePromocao(final Promocao promocao) {
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
		for (final QuantidadeIngrediente ingredienteQtd : ingredientes.values()) {
			final BigDecimal qtd = new BigDecimal(ingredienteQtd.getQuantidade());
			valor = valor.add(ingredienteQtd.getIngrediente().getValor().multiply(qtd));
		}
		return valor;
	}

	public Integer getIngredienteCount(final Ingrediente tipoIngrediente) {
		if (tipoIngrediente.getId() == null) throw new IllegalArgumentException("Não pode contar ingrediente sem id");

		final QuantidadeIngrediente ingredienteQtd = ingredientes.get(tipoIngrediente.getId());
		if (ingredienteQtd == null) return 0;

		return ingredienteQtd.getQuantidade();
	}

	public static final class QuantidadeIngrediente {
		private final Ingrediente ingrediente;
		private final AtomicInteger quantidade;

		public QuantidadeIngrediente(final Ingrediente ingrediente) {
			this.ingrediente = ingrediente;
			this.quantidade = new AtomicInteger(0);
		}

		public Ingrediente getIngrediente() {
			return this.ingrediente;
		}

		public Integer getQuantidade() {
			return this.quantidade.get();
		}

		public Integer aumentaQuantidade(final Integer quanto) {
			return quantidade.addAndGet(quanto);
		}

		public Integer diminuiQuantidade(final Integer quanto) {
			return quantidade.addAndGet(-quanto);
		}
	}
}
