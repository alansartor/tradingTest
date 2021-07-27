package ar.com.signals.trading.trading.support;

public enum TrueFXDivisas {
	EUR_USD("EUR/USD"),
	USD_JPY("USD/JPY"),
	GBP_USD("GBP/USD"),
	EUR_GBP("EUR/GBP"),
	USD_CHF("USD/CHF"),
	EUR_JPY("EUR/JPY"),
	EUR_CHF("EUR/CHF"),
	USD_CAD("USD/CAD"),
	AUD_USD("AUD/USD"),
	GBP_JPY("GBP/JPY"),
	//las siguientes requieren session
	AUD_CAD("AUD/CAD"),
	AUD_CHF("AUD/CHF"),
	AUD_JPY("AUD/JPY"),
	AUD_NZD("AUD/NZD"),
	CAD_CHF("CAD/CHF"),
	CAD_JPY("CAD/JPY"),
	CHF_JPY("CHF/JPY"),
	EUR_AUD("EUR/AUD"),
	EUR_CAD("EUR/CAD"),
	EUR_NOK("EUR/NOK"),
	EUR_NZD("EUR/NZD"),
	GBP_CAD("GBP/CAD"),
	GBP_CHF("GBP/CHF"),
	NZD_JPY("NZD/JPY"),
	NZD_USD("NZD/USD"),
	USD_NOK("USD/NOK"),
	USD_SEK("USD/SEK");
	
	
	private String codigo;
	
	private TrueFXDivisas(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public static TrueFXDivisas getByCode(String code) {
		for (TrueFXDivisas trueFXDivisas : values()) {
			if(trueFXDivisas.getCodigo().equals(code)) {
				return trueFXDivisas;
			}
		}
		return null;
	}

	public static String getCodes() {
		String codigos = "";
		for (TrueFXDivisas trueFXDivisas : values()) {
			codigos += trueFXDivisas.getCodigo() + ",";
		}
		return codigos.length() > 0?codigos.substring(0, codigos.length() - 1):codigos;
	}
}
