package ar.com.signals.trading.util.spring;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import ar.com.signals.trading.util.rest.ListParam;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class CsvHttpMessageConverter<T, L extends ListParam<T>> extends AbstractHttpMessageConverter<L> {
	public static MediaType CSV_MEDIA_TYPE = new MediaType("text", "csv");
	public CsvHttpMessageConverter () {
		super(CSV_MEDIA_TYPE);
	}
	
	public char separator = ';';
	
	@Override
	protected boolean supports (Class<?> clazz) {
	return ListParam.class.isAssignableFrom(clazz);
	}
	
	@Override
	protected L readInternal (Class<? extends L> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
		HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
		Class<T> t = toBeanType(clazz.getGenericSuperclass());
		strategy.setType(t);
		
		Reader reader = new InputStreamReader(inputMessage.getBody());
		CsvToBean csvToBean = new CsvToBeanBuilder(reader)
			       .withType(clazz)
			       .withMappingStrategy(strategy)
			       .withSeparator(separator)
			       .build();
		
		List<T> beanList = csvToBean.parse();//csvToBean.parse(strategy, csv);
		reader.close();
		try {
			L l = clazz.newInstance();
			l.setList(beanList);
			return l;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void writeInternal (L l, HttpOutputMessage outputMessage)  throws IOException, HttpMessageNotWritableException {
	
		HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
		strategy.setType(toBeanType(l.getClass().getGenericSuperclass()));
		
		OutputStreamWriter outputStream = new OutputStreamWriter(outputMessage.getBody());
		StatefulBeanToCsv<T> beanToCsv =
		        new StatefulBeanToCsvBuilder(outputStream)
		                  .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
		                  .withMappingStrategy(strategy)
		                  .build();
		try {
			beanToCsv.write(l.getList());
			outputStream.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> toBeanType (Type type) {
		return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}
}
