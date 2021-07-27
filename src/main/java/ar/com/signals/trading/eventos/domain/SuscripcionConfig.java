package ar.com.signals.trading.eventos.domain;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ar.com.signals.trading.eventos.support.Patron;
import ar.com.signals.trading.seguridad.domain.Usuario;
import ar.com.signals.trading.trading.support.TrueFXDivisas;

@Entity
public class SuscripcionConfig {
	
	@Id
	private Long id;//es el id del sujeto, eso lo hace el MapsId
	
	@MapsId
	@OneToOne
	@JoinColumn(name = "id")
	private Usuario usuario;
	
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
	
	@Column(nullable=true)
	private Long horaInicio;//Hora inicio, hora fin (siempre de 00:00 a 23:59?)
	@Column(nullable=true)
	private Long horaFin;
	
	@Column(nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private Patron patron;
	
	private Timestamp creation_date;
	@Column(nullable = false)//todas las entidades que se sincronizan deben tener este dato obligatoriamente
	private Timestamp last_update_date;
	
	public boolean divisaSeleccionada(TrueFXDivisas trueFXDivisas) {
		switch (trueFXDivisas) {
		case EUR_USD:
			return eur_usd;
		case USD_JPY:
			return usd_jpy;
		case GBP_USD:
			return gbp_usd;
		case EUR_GBP:
			return eur_gbp;
		case USD_CHF:
			return usd_chf;
		case EUR_JPY:
			return eur_jpy;
		case EUR_CHF:
			return eur_chf;
		case USD_CAD:
			return usd_cad;
		case AUD_USD:
			return aud_usd;
		case GBP_JPY:
			return gbp_jpy;
		case AUD_CAD:
			return aud_cad;
		case AUD_CHF:
			return aud_chf;
		case AUD_JPY:
			return aud_jpy;
		case AUD_NZD:
			return aud_nzd;
		case CAD_CHF:
			return cad_chf;
		case CAD_JPY:
			return cad_jpy;
		case CHF_JPY:
			return chf_jpy;
		case EUR_AUD:
			return eur_aud;
		case EUR_CAD:
			return eur_cad;
		case EUR_NOK:
			return eur_nok;
		case EUR_NZD:
			return eur_nzd;
		case GBP_CAD:
			return gbp_cad;
		case GBP_CHF:
			return gbp_chf;
		case NZD_JPY:
			return nzd_jpy;
		case NZD_USD:
			return nzd_usd;
		case USD_NOK:
			return usd_nok;
		case USD_SEK:
			return usd_sek;
		}
		return false;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	public Timestamp getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Timestamp creation_date) {
		this.creation_date = creation_date;
	}
	public Timestamp getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(Timestamp last_update_date) {
		this.last_update_date = last_update_date;
	}
	public Long getHoraInicio() {
		return horaInicio;
	}
	public void setHoraInicio(Long horaInicio) {
		this.horaInicio = horaInicio;
	}
	public Long getHoraFin() {
		return horaFin;
	}
	public void setHoraFin(Long horaFin) {
		this.horaFin = horaFin;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Usuario))//lazy issue, comparar con instanceof
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}
}