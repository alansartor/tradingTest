package ar.com.signals.trading.eventos.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import ar.com.signals.trading.eventos.domain.Suscripcion;
import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.dto.SuscripcionConfigDTO;
import ar.com.signals.trading.eventos.repository.SuscripcionConfigDao;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.seguridad.support.Privilegio;
import ar.com.signals.trading.util.web.FechaUtil;

@Service
@Transactional
public class SuscripcionConfigSrvImpl implements SuscripcionConfigSrv{
	@Resource private SuscripcionConfigDao dao;
	@Resource private SuscripcionSrv suscripcionSrv;

	@Override
	public SuscripcionConfigDTO getSuscripcionConfigDTO(Usuario usuario) {
		SuscripcionConfig entidad = dao.obtener(usuario.getId());
		return new SuscripcionConfigDTO(entidad);
	}

	@Override
	public void validateAndPersist(SuscripcionConfigDTO entidadDTO, BindingResult errors, Usuario usuario) {
		//si habilito las alertas, entonces verifico el rango horario, caso contrario no
		Long minutosIni = null;
		Long minutosFin = null;
		if(entidadDTO.isEventoTradingAlert()) {
			if(entidadDTO.getHoraInicio() == null) {
				errors.rejectValue("horaInicio", "", "Seleccione la hora a la que el sistema debe comenzara a enviar alertas");
			}
			if(entidadDTO.getHoraFin() == null) {
				errors.rejectValue("horaFin", "", "Seleccione la hora a la que el sistema debe dejar de enviar alertas");
			}
			if(entidadDTO.getHoraInicio() != null && entidadDTO.getHoraFin() != null) {
				Date fechaInicio = DateUtils.truncate(entidadDTO.getHoraInicio(), Calendar.DATE);
				minutosIni = FechaUtil.diferencia(entidadDTO.getHoraInicio(), fechaInicio, Calendar.MINUTE);
				Date fechaFin = DateUtils.truncate(entidadDTO.getHoraFin(), Calendar.DATE);
				minutosFin = FechaUtil.diferencia(entidadDTO.getHoraFin(), fechaFin, Calendar.MINUTE);
				if(minutosIni >= minutosFin) {
					errors.rejectValue("horaInicio", "", "La hora de inicio tiene que ser menor que la hora fin. Si quiere recibir alertas las 24 hs, use 00:00 para hora inicio y 23:59 para hora fin");
				}
			}
		}else {
			if(entidadDTO.getHoraInicio() != null) {
				Date fechaInicio = DateUtils.truncate(entidadDTO.getHoraInicio(), Calendar.DATE);
				minutosIni = FechaUtil.diferencia(fechaInicio, entidadDTO.getHoraInicio(), Calendar.MINUTE);
			}
			if(entidadDTO.getHoraFin() != null) {
				Date fechaFin = DateUtils.truncate(entidadDTO.getHoraFin(), Calendar.DATE);
				minutosFin = FechaUtil.diferencia(fechaFin, entidadDTO.getHoraFin(), Calendar.MINUTE);
			}
		}
		
		if(errors.hasErrors()) {
			return;
		}
		SuscripcionConfig entidad = dao.obtener(usuario.getId());
		Timestamp fechaActual = new Timestamp(new Date().getTime());
		boolean nuevo = false;
		if(entidad == null) {
			nuevo = true;
			entidad = new SuscripcionConfig();
			entidad.setUsuario(usuario);
			entidad.setCreation_date(fechaActual);
		}
		entidad.setLast_update_date(fechaActual);
		entidad.setEventoTradingAlert(entidadDTO.isEventoTradingAlert());
		if(entidadDTO.isEventoTradingAlert()) {
			Suscripcion suscripcion = suscripcionSrv.getBySucursPepedTipoEvento(usuario, Privilegio.EVENTO_TRADING_ALERT);
			if(suscripcion == null) {
				//creo la suscripcion
				suscripcion = new Suscripcion();
				suscripcion.setUsuario(usuario);
				suscripcion.setEvento(Privilegio.EVENTO_TRADING_ALERT);
				suscripcion.setCreation_date(fechaActual);
				suscripcion.setLast_update_date(fechaActual);
				suscripcionSrv.guardar(suscripcion);
			}
		}else if(!nuevo) {
			//elimino la suscripcion
			suscripcionSrv.eliminar(usuario, Privilegio.EVENTO_TRADING_ALERT);
		}
		entidad.setHoraInicio(minutosIni);
		entidad.setHoraFin(minutosFin);
		entidad.setEur_usd(entidadDTO.isEur_usd());
		entidad.setUsd_jpy(entidadDTO.isUsd_jpy());
		entidad.setGbp_usd(entidadDTO.isGbp_usd());
		entidad.setEur_gbp(entidadDTO.isEur_gbp());
		entidad.setUsd_chf(entidadDTO.isUsd_chf());
		entidad.setEur_jpy(entidadDTO.isEur_jpy());
		entidad.setEur_chf(entidadDTO.isEur_chf());
		entidad.setUsd_cad(entidadDTO.isUsd_cad());
		entidad.setAud_usd(entidadDTO.isAud_usd());
		
		entidad.setGbp_jpy(entidadDTO.isGbp_jpy());
		entidad.setAud_cad(entidadDTO.isAud_cad());
		entidad.setAud_chf(entidadDTO.isAud_chf());
		entidad.setAud_jpy(entidadDTO.isAud_jpy());
		entidad.setAud_nzd(entidadDTO.isAud_nzd());
		entidad.setCad_chf(entidadDTO.isCad_chf());
		entidad.setCad_jpy(entidadDTO.isCad_jpy());
		entidad.setChf_jpy(entidadDTO.isChf_jpy());
		entidad.setEur_aud(entidadDTO.isEur_aud());
		entidad.setEur_cad(entidadDTO.isEur_cad());
		entidad.setEur_nok(entidadDTO.isEur_nok());
		entidad.setEur_nzd(entidadDTO.isEur_nzd());
		entidad.setGbp_cad(entidadDTO.isGbp_cad());
		entidad.setGbp_chf(entidadDTO.isGbp_chf());
		entidad.setNzd_jpy(entidadDTO.isNzd_jpy());
		entidad.setNzd_usd(entidadDTO.isNzd_usd());
		entidad.setUsd_nok(entidadDTO.isUsd_nok());
		entidad.setUsd_sek(entidadDTO.isUsd_sek());
		
		entidad.setPatron(entidadDTO.getPatron());
		if(nuevo) {
			dao.guardar(entidad);
		}
	}

	@Override
	public List<SuscripcionConfig> getTradingAlertSuscript(long minutos) {
		return dao.getTradingAlertSuscript(minutos);
	}

	
}