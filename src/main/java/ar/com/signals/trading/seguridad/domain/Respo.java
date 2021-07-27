package ar.com.signals.trading.seguridad.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.PrivilegioDeserializer;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;

@Entity
@Table(uniqueConstraints= {
		@UniqueConstraint(columnNames={"codigo"})})
public class Respo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//esto solo usar en datos numericos, si lo hacemos en un string, mysql arroja error: Incorrect column specifier for column 'id'
	private Long id;//antes el id era un string, pero como dejo crear responsabilidades custom, entonces cambio a id numerica, y lo que era el id lo paso a codigo
	@Column(length=60)
	private String codigo;
	private String descripcion;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length=30)
	private TipoResponsabilidad tipo;
	
	//no se necesita para mysql@Column(columnDefinition = "number(1) default 0")
	private boolean activo;
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;
	
	@JsonDeserialize(using = PrivilegioDeserializer.class)//Esto es para ignorar privilegios quiza existen en el servidor central pero todavia no existen en el servidor de planta por ser que esta desactualizado
	@ElementCollection(targetClass=Privilegio.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING) // Possibly optional (I'm not sure) but defaults to ORDINAL.
    @CollectionTable(name="respo_privilegio")
    @Column(name="privilegio", length=60) // Column name in respo_privilegio
	@OrderColumn//el order column es para list, no para Set, entonces dejo como list
	private List<Privilegio> privilegios;//el inicializarlo con un array empty hace que cada vez que referenciemos la lista se vuelve a inicializar
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Privilegio> getPrivilegios() {
		return privilegios;
	}
	public void setPrivilegios(List<Privilegio> privilegios) {
		this.privilegios = privilegios;
	}
	public Timestamp getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Timestamp creation_date) {
		this.creation_date = creation_date;
	}
	public Timestamp getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(Timestamp last_update_date) {
		this.last_update_date = last_update_date;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public TipoResponsabilidad getTipo() {
		return tipo;
	}
	public void setTipo(TipoResponsabilidad tipo) {
		this.tipo = tipo;
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
		if (!(obj instanceof Usuario))//lazy issue, comparar con instanceof
			return false;
		Respo other = (Respo) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return codigo + (descripcion != null?" (" + descripcion + ")":"");
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
}