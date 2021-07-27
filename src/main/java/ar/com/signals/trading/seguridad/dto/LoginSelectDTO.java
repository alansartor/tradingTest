package ar.com.signals.trading.seguridad.dto;

public class LoginSelectDTO {
	private Long responsabilidadId;
	private Long firmaId;
	private Long sucursalId;
	
	private String user_target_url;

	public Long getResponsabilidadId() {
		return responsabilidadId;
	}
	public void setResponsabilidadId(Long responsabilidadId) {
		this.responsabilidadId = responsabilidadId;
	}
	public Long getFirmaId() {
		return firmaId;
	}
	public void setFirmaId(Long firmaId) {
		this.firmaId = firmaId;
	}
	public Long getSucursalId() {
		return sucursalId;
	}
	public void setSucursalId(Long sucursalId) {
		this.sucursalId = sucursalId;
	}
	public String getUser_target_url() {
		return user_target_url;
	}
	public void setUser_target_url(String user_target_url) {
		this.user_target_url = user_target_url;
	}
}
