package ar.com.signals.trading.eventos.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.ibm.icu.util.Calendar;

import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.eventos.support.Patron;

public class SuscripcionConfigDTO {
	private boolean eventoTradingAlert;

	private boolean eur_usd;
	private boolean usd_jpy;
	private boolean gbp_usd;
	private boolean eur_gbp;
	private boolean usd_chf;
	private boolean eur_jpy;
	private boolean eur_chf;
	private boolean usd_cad;
	private boolean aud_usd;
	private boolean gbp_jpy;
	private boolean aud_cad;
	private boolean aud_chf;
	private boolean aud_jpy;
	private boolean aud_nzd;
	private boolean cad_chf;
	private boolean cad_jpy;
	private boolean chf_jpy;
	private boolean eur_aud;
	private boolean eur_cad;
	private boolean eur_nok;
	private boolean eur_nzd;
	private boolean gbp_cad;
	private boolean gbp_chf;
	private boolean nzd_jpy;
	private boolean nzd_usd;
	private boolean usd_nok;
	private boolean usd_sek;
	
	@DateTimeFormat(pattern="HH:mm")
	private Date horaInicio;//Hora inicio, hora fin (siempre de 00:00 a 23:59?)
	@DateTimeFormat(pattern="HH:mm")
	private Date horaFin;
	
	@NotNull
	private Patron patron;
	
	public SuscripcionConfigDTO() {}
	 
	public SuscripcionConfigDTO(SuscripcionConfig entidad) {
		if(entidad != null) {
			eventoTradingAlert = entidad.isEventoTradingAlert();
			eur_usd = entidad.isEur_usd();
			usd_jpy = entidad.isUsd_jpy();
			gbp_usd = entidad.isGbp_usd();
			eur_gbp = entidad.isEur_gbp();
			usd_chf = entidad.isUsd_chf();
			eur_jpy = entidad.isEur_jpy();
			eur_chf = entidad.isEur_chf();
			usd_cad = entidad.isUsd_cad();
			aud_usd = entidad.isAud_usd();
			gbp_jpy = entidad.isGbp_jpy();
			aud_cad = entidad.isAud_cad();
			aud_chf = entidad.isAud_chf();
			aud_jpy = entidad.isAud_jpy();
			aud_nzd = entidad.isAud_nzd();
			cad_chf = entidad.isCad_chf();
			cad_jpy = entidad.isCad_jpy();
			chf_jpy = entidad.isChf_jpy();
			eur_aud = entidad.isEur_aud();
			eur_cad = entidad.isEur_cad();
			eur_nok = entidad.isEur_nok();
			eur_nzd = entidad.isEur_nzd();
			gbp_cad = entidad.isGbp_cad();
			gbp_chf = entidad.isGbp_chf();
			nzd_jpy = entidad.isNzd_jpy();
			nzd_usd = entidad.isNzd_usd();
			usd_nok = entidad.isUsd_nok();
			usd_sek = entidad.isUsd_sek();
			if(entidad.getHoraInicio() != null) {
				Date date = DateUtils.truncate(new Date(), Calendar.DATE);
				horaInicio = DateUtils.addMinutes(date, entidad.getHoraInicio().intValue());
			}
			if(entidad.getHoraFin() != null) {
				Date date = DateUtils.truncate(new Date(), Calendar.DATE);
				horaFin = DateUtils.addMinutes(date, entidad.getHoraFin().intValue());
			}
			patron = entidad.getPatron();
		}
	}

	public boolean isEventoTradingAlert() {
		return eventoTradingAlert;
	}

	public void setEventoTradingAlert(boolean eventoTradingAlert) {
		this.eventoTradingAlert = eventoTradingAlert;
	}

	public boolean isEur_usd() {
		return eur_usd;
	}

	public void setEur_usd(boolean eur_usd) {
		this.eur_usd = eur_usd;
	}

	public boolean isUsd_jpy() {
		return usd_jpy;
	}

	public void setUsd_jpy(boolean usd_jpy) {
		this.usd_jpy = usd_jpy;
	}

	public boolean isGbp_usd() {
		return gbp_usd;
	}

	public void setGbp_usd(boolean gbp_usd) {
		this.gbp_usd = gbp_usd;
	}

	public boolean isEur_gbp() {
		return eur_gbp;
	}

	public void setEur_gbp(boolean eur_gbp) {
		this.eur_gbp = eur_gbp;
	}

	public boolean isUsd_chf() {
		return usd_chf;
	}

	public void setUsd_chf(boolean usd_chf) {
		this.usd_chf = usd_chf;
	}

	public boolean isEur_jpy() {
		return eur_jpy;
	}

	public void setEur_jpy(boolean eur_jpy) {
		this.eur_jpy = eur_jpy;
	}

	public boolean isEur_chf() {
		return eur_chf;
	}

	public void setEur_chf(boolean eur_chf) {
		this.eur_chf = eur_chf;
	}

	public boolean isUsd_cad() {
		return usd_cad;
	}

	public void setUsd_cad(boolean usd_cad) {
		this.usd_cad = usd_cad;
	}

	public boolean isAud_usd() {
		return aud_usd;
	}

	public void setAud_usd(boolean aud_usd) {
		this.aud_usd = aud_usd;
	}

	public Date getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Date getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Date horaFin) {
		this.horaFin = horaFin;
	}

	public boolean isGbp_jpy() {
		return gbp_jpy;
	}

	public void setGbp_jpy(boolean gbp_jpy) {
		this.gbp_jpy = gbp_jpy;
	}

	public boolean isAud_cad() {
		return aud_cad;
	}

	public void setAud_cad(boolean aud_cad) {
		this.aud_cad = aud_cad;
	}

	public boolean isAud_chf() {
		return aud_chf;
	}

	public void setAud_chf(boolean aud_chf) {
		this.aud_chf = aud_chf;
	}

	public boolean isAud_jpy() {
		return aud_jpy;
	}

	public void setAud_jpy(boolean aud_jpy) {
		this.aud_jpy = aud_jpy;
	}

	public boolean isAud_nzd() {
		return aud_nzd;
	}

	public void setAud_nzd(boolean aud_nzd) {
		this.aud_nzd = aud_nzd;
	}

	public boolean isCad_chf() {
		return cad_chf;
	}

	public void setCad_chf(boolean cad_chf) {
		this.cad_chf = cad_chf;
	}

	public boolean isCad_jpy() {
		return cad_jpy;
	}

	public void setCad_jpy(boolean cad_jpy) {
		this.cad_jpy = cad_jpy;
	}

	public boolean isChf_jpy() {
		return chf_jpy;
	}

	public void setChf_jpy(boolean chf_jpy) {
		this.chf_jpy = chf_jpy;
	}

	public boolean isEur_aud() {
		return eur_aud;
	}

	public void setEur_aud(boolean eur_aud) {
		this.eur_aud = eur_aud;
	}

	public boolean isEur_cad() {
		return eur_cad;
	}

	public void setEur_cad(boolean eur_cad) {
		this.eur_cad = eur_cad;
	}

	public boolean isEur_nok() {
		return eur_nok;
	}

	public void setEur_nok(boolean eur_nok) {
		this.eur_nok = eur_nok;
	}

	public boolean isEur_nzd() {
		return eur_nzd;
	}

	public void setEur_nzd(boolean eur_nzd) {
		this.eur_nzd = eur_nzd;
	}

	public boolean isGbp_cad() {
		return gbp_cad;
	}

	public void setGbp_cad(boolean gbp_cad) {
		this.gbp_cad = gbp_cad;
	}

	public boolean isGbp_chf() {
		return gbp_chf;
	}

	public void setGbp_chf(boolean gbp_chf) {
		this.gbp_chf = gbp_chf;
	}

	public boolean isNzd_jpy() {
		return nzd_jpy;
	}

	public void setNzd_jpy(boolean nzd_jpy) {
		this.nzd_jpy = nzd_jpy;
	}

	public boolean isNzd_usd() {
		return nzd_usd;
	}

	public void setNzd_usd(boolean nzd_usd) {
		this.nzd_usd = nzd_usd;
	}

	public boolean isUsd_nok() {
		return usd_nok;
	}

	public void setUsd_nok(boolean usd_nok) {
		this.usd_nok = usd_nok;
	}

	public boolean isUsd_sek() {
		return usd_sek;
	}

	public void setUsd_sek(boolean usd_sek) {
		this.usd_sek = usd_sek;
	}

	public Patron getPatron() {
		return patron;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
	}
}
