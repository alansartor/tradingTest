package ar.com.signals.trading.seguridad.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.AutoPopulatingList;

import ar.com.signals.trading.seguridad.support.ILoginSeleccion;
import ar.com.signals.trading.seguridad.support.TipoResponsabilidad;

public class ResponsabilidadDTO implements Serializable, ILoginSeleccion{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotBlank
	@Size(max = 60)
	private String codigo;
	private String descripcion;
	@NotNull
	private TipoResponsabilidad tipo;
	private List<PrivilegioDTO> privilegios = new AutoPopulatingList<PrivilegioDTO>(PrivilegioDTO.class);

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcionCompleta() {
		return codigo + (descripcion != null?" (" + descripcion + ")":"");
	}

	@Override
	public String toString() {
		return getDescripcionCompleta();
	}
	@Override
	public String getDescripcion1() {//se usa como texto en login select
		return codigo;
	}
	@Override
	public String getDescripcion2() {//se usa como subtexto en login select
		return  descripcion != null?" (" + descripcion + ")":"";
	}

	public List<PrivilegioDTO> getPrivilegios() {
		return privilegios;
	}

	public void setPrivilegios(List<PrivilegioDTO> privilegios) {
		this.privilegios = privilegios;
	}

	public TipoResponsabilidad getTipo() {
		return tipo;
	}

	public void setTipo(TipoResponsabilidad tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
