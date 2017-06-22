package br.com.dextra.dex.server.domain;

/**
 * Classe abstrata representado todas entidades persistíveis no sistema.
 * Essa classe garante que todas as Entidades possuem um ID e que suas identidades são determinadas por esse ID. Também implementa
 * {@code Object#toString()} para mostar o tipo de Entidade e ID
 *
 * @author Felipe
 */
public abstract class Entidade {

	private Integer id;

	public Entidade() {
		// Construtor vazio
	}

	public Entidade(final Integer id) {
		this.id = id;
	}

	public Entidade(final Entidade entidade) {
		this.id = entidade.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final Entidade other = (Entidade) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s [id=%s]", getClass().getSimpleName(), id);
	}
}
