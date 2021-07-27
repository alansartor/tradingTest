package ar.com.signals.trading.util.web;

import java.util.Collection;

//@JsonTypeName("datasets")                                                                                         
//@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT ,use = JsonTypeInfo.Id.NAME)
public class JQueryChartData <T extends ChartLineDataDTO>{
	private final Collection<T> datasets;

    public JQueryChartData(final Collection<T> datasets){
        this.datasets = datasets;

    }

	public Collection<T> getDatasets() {
		return datasets;
	}
}
