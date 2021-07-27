package ar.com.signals.trading.util.rest;

import java.util.List;

public class ListParam<T> {
	private List<T> list;

	public List<T> getList () {
		return list;
	}
  
	public void setList (List<T> list) {
		this.list = list;
	}
}