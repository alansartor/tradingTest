package ar.com.signals.trading.configuracion.support;

public enum TipoArchivoImportarEnum {
	Agroindustria_fertilizantes("Fertilizantes", "https://www.argentina.gob.ar/senasa/programas-sanitarios/productos-veterinarios-fitosanitarios-y-fertilizantes/fertilizantes-enmiendas-y-otros"),
	Agroindustria_agroquimicos("Otros Agroquimicos", "https://www.argentina.gob.ar/senasa/programas-sanitarios/productos-veterinarios-fitosanitarios-y-fertilizantes/registro-nacional-de-terapeutica-vegetal");
	
	private String descripcion;
	private String web;
	
	private TipoArchivoImportarEnum(String descripcion, String web) {
		this.descripcion = descripcion;
		this.web = web;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}
}
