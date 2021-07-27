package ar.com.signals.trading.configuracion.dto;

public class GenerarEntidadesFacturaDTO {
	private boolean generarAlicuotasIVA;
	private boolean generarCondicionesIVA;
	//private boolean generarCotizacionMoneda;
	private boolean generarMonedas;
	private boolean generarTiposComprobante;
	private boolean generarTiposDocumento;
	private boolean generarTiposTributo;
	private boolean generarUnidadesMedida;
	private boolean generarTiposDatosAdicionales;
	private boolean generarCondicionesPago;
	private boolean generarConceptos;//tipo deduccciones y retenciones para liquidacion primaria (si bien estan en el web service, lo hacemos manual)
	
	public boolean isGenerarAlicuotasIVA() {
		return generarAlicuotasIVA;
	}
	public void setGenerarAlicuotasIVA(boolean generarAlicuotasIVA) {
		this.generarAlicuotasIVA = generarAlicuotasIVA;
	}
	public boolean isGenerarCondicionesIVA() {
		return generarCondicionesIVA;
	}
	public void setGenerarCondicionesIVA(boolean generarCondicionesIVA) {
		this.generarCondicionesIVA = generarCondicionesIVA;
	}
/*	public boolean isGenerarCotizacionMoneda() {
		return generarCotizacionMoneda;
	}
	public void setGenerarCotizacionMoneda(boolean generarCotizacionMoneda) {
		this.generarCotizacionMoneda = generarCotizacionMoneda;
	}*/
	public boolean isGenerarMonedas() {
		return generarMonedas;
	}
	public void setGenerarMonedas(boolean generarMonedas) {
		this.generarMonedas = generarMonedas;
	}
	public boolean isGenerarTiposComprobante() {
		return generarTiposComprobante;
	}
	public void setGenerarTiposComprobante(boolean generarTiposComprobante) {
		this.generarTiposComprobante = generarTiposComprobante;
	}
	public boolean isGenerarTiposDocumento() {
		return generarTiposDocumento;
	}
	public void setGenerarTiposDocumento(boolean generarTiposDocumento) {
		this.generarTiposDocumento = generarTiposDocumento;
	}
	public boolean isGenerarTiposTributo() {
		return generarTiposTributo;
	}
	public void setGenerarTiposTributo(boolean generarTiposTributo) {
		this.generarTiposTributo = generarTiposTributo;
	}
	public boolean isGenerarUnidadesMedida() {
		return generarUnidadesMedida;
	}
	public void setGenerarUnidadesMedida(boolean generarUnidadesMedida) {
		this.generarUnidadesMedida = generarUnidadesMedida;
	}
	public boolean isGenerarTiposDatosAdicionales() {
		return generarTiposDatosAdicionales;
	}
	public void setGenerarTiposDatosAdicionales(boolean generarTiposDatosAdicionales) {
		this.generarTiposDatosAdicionales = generarTiposDatosAdicionales;
	}
	public boolean isGenerarCondicionesPago() {
		return generarCondicionesPago;
	}
	public void setGenerarCondicionesPago(boolean generarCondicionesPago) {
		this.generarCondicionesPago = generarCondicionesPago;
	}
	public boolean isGenerarConceptos() {
		return generarConceptos;
	}
	public void setGenerarConceptos(boolean generarConceptos) {
		this.generarConceptos = generarConceptos;
	}
}
