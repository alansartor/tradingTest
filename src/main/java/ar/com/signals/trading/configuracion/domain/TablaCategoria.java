package ar.com.signals.trading.configuracion.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
//@Table(indexes = {@Index(columnList = "usuario_username", name = "respoUsuario_index")})
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"valor", "categoria_id"}))//Nombre de las columnas en la base de datos
public class TablaCategoria extends AbstractCategoria{
	@Column(precision = 19, scale = 2)
	private BigDecimal valor;//private Double valor;
	@Column(precision = 19, scale = 2)
	private BigDecimal valorRelacionado;//private Double valorRelacionado;

	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValorRelacionado() {
		return valorRelacionado;
	}
	public void setValorRelacionado(BigDecimal valorRelacionado) {
		this.valorRelacionado = valorRelacionado;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TablaCategoria))//lazy issue, comparar con instanceof
			return false;
		TablaCategoria other = (TablaCategoria) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}
