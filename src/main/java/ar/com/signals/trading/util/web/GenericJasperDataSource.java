package ar.com.signals.trading.util.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class GenericJasperDataSource implements JRDataSource{
	private final List<?> registros;
	private int indexActual = -1;
	private Map<String, Method> metodos;
	private Class<?> clase;
	
	public <E> GenericJasperDataSource(Class<E> clase, List<E> registros, String[] fieldNames) {
		this.registros = registros;
		this.clase = clase;
		Method[] methods = clase.getMethods();
		metodos = new HashMap<String, Method>();
		

		for (int i = 0; i < fieldNames.length; i++) {	
			//Busco el metodo para get de ese field
			for (Method method : methods){
		        if ((method.getName().startsWith("get")) && (method.getName().substring(3).equalsIgnoreCase(fieldNames[i]))){
		        	metodos.put(fieldNames[i], method);
		        	continue;
		        }else if ((method.getName().startsWith("is")) && (method.getName().substring(2).equalsIgnoreCase(fieldNames[i]))){
		        	metodos.put(fieldNames[i], method);
		        	continue;
		        }
		    }
		}
	}

	public Object getFieldValue(JRField jrField) throws JRException{
		try {
			return metodos.get(jrField.getName()).invoke(registros.get(indexActual));
		} catch (Exception e) {
			throw new JRException("Error al buscar propiedad " + jrField.getName() + " de la clase " + clase.getName(), e);
		}
	}

	public boolean next() throws JRException{
		return ++indexActual < registros.size();
	}

	public void resetIndex() {
		indexActual = -1;
	}
}
