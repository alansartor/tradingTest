package ar.com.signals.trading.configuracion.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import ar.com.signals.trading.configuracion.support.ImportarColumnaEnum;
import ar.com.signals.trading.configuracion.support.TipoArchivoImportarEnum;

public class ImportarDeArchivoDTO {
	@NotNull
	private ImportarColumnaEnum columnaIgnorar;
	@NotNull
	private TipoArchivoImportarEnum tipoArchivo;
	
	@NotNull//no funcona con multipart
	private MultipartFile archivo;
	
	public ImportarDeArchivoDTO() {
		super();
	}

	public ImportarColumnaEnum getColumnaIgnorar() {
		return columnaIgnorar;
	}

	public void setColumnaIgnorar(ImportarColumnaEnum columnaIgnorar) {
		this.columnaIgnorar = columnaIgnorar;
	}

	public MultipartFile getArchivo() {
		return archivo;
	}

	public void setArchivo(MultipartFile archivo) {
		this.archivo = archivo;
	}

	public TipoArchivoImportarEnum getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(TipoArchivoImportarEnum tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}
	
	
}
