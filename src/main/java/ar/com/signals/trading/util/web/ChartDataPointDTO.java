package ar.com.signals.trading.util.web;

public class ChartDataPointDTO {
	private Object x;
	private Object y;
	 
	public ChartDataPointDTO() {}
	
	public ChartDataPointDTO(Object x, Object y) {
		this.x = x;
		this.y = y;
	}
	public Object getX() {
		return x;
	}
	public void setX(Object x) {
		this.x = x;
	}
	public Object getY() {
		return y;
	}
	public void setY(Object y) {
		this.y = y;
	}
}
