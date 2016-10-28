package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CidadeSerializer extends StdSerializer<Cidade> {
     
    public CidadeSerializer() {
        this(null);
    }
   
    public CidadeSerializer(Class<Cidade> t) {
        super(t);
    }
 
    @Override
    public void serialize(Cidade value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {        
        jgen.writeStartObject();
        jgen.writeNumberField("cidadeId", value.getId());
        jgen.writeStringField("cidadeNome", value.getNome());
        jgen.writeNumberField("estadoId", value.getEstado().getId());
        jgen.writeStringField("estadoNome", value.getEstado().getNome());
        jgen.writeEndObject();
    }    
    
    public static String serialize(Cidade cidade, Estado estado) throws JsonProcessingException {
        cidade.getEstado().setNome(estado.getNome());
        
        ObjectMapper mapper = new ObjectMapper();
 
        SimpleModule module = new SimpleModule();
        module.addSerializer(Cidade.class, new CidadeSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsString(cidade);
    }
}
