package ar.com.signals.trading.util.web;

import java.util.ArrayList;
import java.util.List;

public class ChartLineDataDTO<E> {
	private String label;
	private boolean fill = false;//para no pintar debajo
	private String borderColor;//color de la linea
	private String backgroundColor;//color del relleno (del punto)
	private String type;//es el tipo de chart
	
	protected List<E> data = new ArrayList<>();
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<E> getData() {
		return data;
	}
	public void setData(List<E> data) {
		this.data = data;
	}
	public boolean isFill() {
		return fill;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	public String getBorderColor() {
		return borderColor;
	}
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
