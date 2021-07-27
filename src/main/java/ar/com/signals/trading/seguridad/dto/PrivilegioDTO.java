package ar.com.signals.trading.seguridad.dto;

import ar.com.signals.trading.seguridad.support.Privilegio;

public class PrivilegioDTO {
	private Privilegio privilegio;
	private boolean checked;
	
	public PrivilegioDTO() {}
	
	public PrivilegioDTO(Privilegio privilegio, boolean checked) {
		this.privilegio = privilegio;
		this.checked = checked;
	}
	
	public Privilegio getPrivilegio() {
		return privilegio;
	}
	public void setPrivilegio(Privilegio privilegio) {
		this.privilegio = privilegio;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((privilegio == null) ? 0 : privilegio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivilegioDTO other = (PrivilegioDTO) obj;
		if (privilegio != other.privilegio)
			return false;
		return true;
	}
	
	
}
