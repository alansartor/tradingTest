package ar.com.signals.trading.seguridad.dto;

import java.util.Date;

public class TokenTemporalDTO {
	private String token;
	private Date expirationTime;
	
	public TokenTemporalDTO() {}
	
	public TokenTemporalDTO(Date expirationTime, String token) {
		this.token = token;
		this.expirationTime = expirationTime;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
}
