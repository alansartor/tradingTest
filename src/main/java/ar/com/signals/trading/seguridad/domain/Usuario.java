package ar.com.signals.trading.seguridad.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ar.com.signals.trading.general.support.Modulo;
import ar.com.signals.trading.seguridad.dto.MenuOption;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.seguridad.support.TipoPrivilegio;

@Entity
@Table(uniqueConstraints= {
		@UniqueConstraint(columnNames={"username"}),
		@UniqueConstraint(columnNames={"user_email"}),
		@UniqueConstraint(columnNames={"telegram_id"})})//Nombre de las columnas en la base de datos
public class Usuario implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//esto solo usar en datos numericos
	private Long id;//es el id del sujeto, eso lo hace el MapsId
	@Column(length=20, nullable=false)
	private String username;
	private String password;
	@Column(length=40, nullable=true)
	private String user_email;
	//no se necesita para mysql@Column(columnDefinition = "number(1) default 0")
	private boolean user_enabled;
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;
	
	@Column(length=60, nullable = false)
	private String descripcion;
	
	@Column(length=60, nullable = true)
	private String observaciones;
	
	@Column(length=20)//unique
	private String telegram_id;//se usa para enviar alertas y notificaciones por telegram. Para poder obtener este id, el usuario tiene que mandar un token (temporal) al bot del sistema por telegram, entonces ahi obtenemos este valor y lo asociamos al usuario
	
	@Transient
	private Respo responsabilidad;
	@Transient
	private Map<Modulo, MenuOption> menu;
	@Transient
	private Map<Modulo, MenuOption> menuFavoritos;
	@Transient
	private String menuImagen;//es la imagen que se muestra en el menu, por ahora solo cuando alguien inicia session con una respo especial, es decir no una comun que esta relacionada a una sucursal
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(responsabilidad != null){
			return responsabilidad.getPrivilegios();
		}
		//Antes que el usuario seleccione una responsabilidad, este no tiene privilegios. Ver si lanzar excepcion aca y que
		//en algun lado automaticamente se redireccione a la pantalla de seleccion de responsabilidad
		return new ArrayList<GrantedAuthority>();
	}
	
	public List<Privilegio> getPrivilegiosEvento() {
		if(responsabilidad != null){
			List<Privilegio> eventos = new ArrayList<>();
			for (Privilegio p : responsabilidad.getPrivilegios()) {
				if(TipoPrivilegio.PrivilegioEvento.equals(p.getTipoPrivilegio())) {
					eventos.add(p);
				}
			}
			return eventos;
		}
		return new ArrayList<Privilegio>();
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public boolean isUser_enabled() {
		return user_enabled;
	}
	public void setUser_enabled(boolean user_enabled) {
		this.user_enabled = user_enabled;
	}
	public Respo getResponsabilidad() {
		return responsabilidad;
	}
	public void setResponsabilidad(Respo responsabilidad) {
		this.responsabilidad = responsabilidad;
	}
	public Map<Modulo, MenuOption> getMenu() {
		return menu;
	}
	public void setMenu(Map<Modulo, MenuOption> menu) {
		this.menu = menu;
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
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}	
	@Override
	public boolean isAccountNonExpired() {
		return user_enabled;
	}
	@Override
	public boolean isAccountNonLocked() {
		return user_enabled;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return user_enabled;
	}
	@Override
	public boolean isEnabled() {
		return user_enabled;
	}
	public Map<Modulo, MenuOption> getMenuFavoritos() {
		return menuFavoritos;
	}
	public void setMenuFavoritos(Map<Modulo, MenuOption> menuFavoritos) {
		this.menuFavoritos = menuFavoritos;
	}

	public String getMenuImagen() {
		return menuImagen;
	}

	public void setMenuImagen(String menuImagen) {
		this.menuImagen = menuImagen;
	}

	public String getTelegram_id() {
		return telegram_id;
	}

	public void setTelegram_id(String telegram_id) {
		this.telegram_id = telegram_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
