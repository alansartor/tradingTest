package ar.com.signals.trading.configuracion.dto;

public class GenerarProductosDTO {
	private boolean generarProductosInase;
	private boolean generarEmpresasAgroquimicos;//generarProductosAgroquimicos;
	private boolean generarCategoriasEmpleados;
	private boolean generarClientes;
	private boolean generarSucursales;
	private boolean generarPlagas;

	public GenerarProductosDTO() {
		super();
	}
	
	public GenerarProductosDTO(boolean generarProductosInase, boolean generarEmpresasAgroquimicos) {
		super();
		this.generarProductosInase = generarProductosInase;
		this.generarEmpresasAgroquimicos = generarEmpresasAgroquimicos;
	}

	public boolean isGenerarProductosInase() {
		return generarProductosInase;
	}

	public void setGenerarProductosInase(boolean generarProductosInase) {
		this.generarProductosInase = generarProductosInase;
	}

	public boolean isGenerarCategoriasEmpleados() {
		return generarCategoriasEmpleados;
	}

	public void setGenerarCategoriasEmpleados(boolean generarCategoriasEmpleados) {
		this.generarCategoriasEmpleados = generarCategoriasEmpleados;
	}

	public boolean isGenerarSucursales() {
		return generarSucursales;
	}

	public void setGenerarSucursales(boolean generarSucursales) {
		this.generarSucursales = generarSucursales;
	}

	public boolean isGenerarClientes() {
		return generarClientes;
	}

	public void setGenerarClientes(boolean generarClientes) {
		this.generarClientes = generarClientes;
	}

	public boolean isGenerarEmpresasAgroquimicos() {
		return generarEmpresasAgroquimicos;
	}

	public void setGenerarEmpresasAgroquimicos(boolean generarEmpresasAgroquimicos) {
		this.generarEmpresasAgroquimicos = generarEmpresasAgroquimicos;
	}

	public boolean isGenerarPlagas() {
		return generarPlagas;
	}

	public void setGenerarPlagas(boolean generarPlagas) {
		this.generarPlagas = generarPlagas;
	}
}
