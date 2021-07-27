package ar.com.signals.trading.seguridad.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PrivilegioDeserializer extends JsonDeserializer<List<Privilegio>> {

    @Override 
    public List<Privilegio> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
    	List<Privilegio> lista = new ArrayList<Privilegio>();
    	List<String> listaString = jsonParser.readValueAs(new TypeReference<List<String>>(){});
    	Privilegio p = null;
    	for (String string : listaString) {
			p = Privilegio.valueOf(string);
			if(p != null){
				lista.add(p);
			}
		}
        return lista;
    }
}
