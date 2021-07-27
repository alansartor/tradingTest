package ar.com.signals.trading.eventos.service;

import java.util.List;

import org.springframework.validation.BindingResult;

import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.dto.SuscripcionConfigDTO;
import ar.com.signals.trading.eventos.dto.SuscripcionDTO;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;

public interface SuscripcionConfigSrv {

	SuscripcionConfigDTO getSuscripcionConfigDTO(Usuario usuario);

	void validateAndPersist(SuscripcionConfigDTO entidadDTO, BindingResult errors, Usuario usuario);

	List<SuscripcionConfig> getTradingAlertSuscript(long minutos);

}
