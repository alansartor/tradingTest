package ar.com.signals.trading.trading.support;

public enum TipoAlertaTrading {
	PMG_ALCISTA("Alcista (PMG)"),
	XXL_ALCISTA("Alcista (XXL)"),
	VRV_ALCISTA("Alcista (VRV)"),
	PMG_BAJISTA("Bajista (PMG)"),
	XXL_BAJISTA("Bajista (XXL)"),
	RVR_BAJISTA("Bajista (RVR)"),
	RUPTURA_RESISTENCIA("Ruptura Resistencia"),
	RUPTURA_SOPORTE("Ruptura Soporte");
	
	private String descripcion;

	private TipoAlertaTrading(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
