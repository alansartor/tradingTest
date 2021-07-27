package ar.com.signals.trading.util.web;

import java.util.Collection;
import java.util.List;

public class JQueryChart <T extends ChartLineDataDTO>{
    private JQueryChartData<T> data;
    private List<Mensaje> mensajes;
    
    public JQueryChart(final Collection<T> registros){
        this.data = new JQueryChartData<T>(registros);
    }
    
    public JQueryChart(final Collection<T> registros, List<Mensaje> mensajes){
        this.data = new JQueryChartData<T>(registros);
        this.mensajes = mensajes;
    }

	public JQueryChartData<T> getData() {
		return data;
	}

	public void setData(JQueryChartData<T> data) {
		this.data = data;
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
}
