package aplicacaofinanceira.util;

import aplicacaofinanceira.model.ClientePessoaFisica;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ClientePessoaFisicaSerializer extends StdSerializer<ClientePessoaFisica> {

    public ClientePessoaFisicaSerializer() {
        this(null);
    }

    public ClientePessoaFisicaSerializer(Class<ClientePessoaFisica> t) {
        super(t);
    }

    @Override
    public void serialize(ClientePessoaFisica value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField(Constants.CLIENTE_ID, value.getId());
        jgen.writeStringField(Constants.CLIENTE_NOME, value.getNome());
        jgen.writeStringField(Constants.CLIENTE_STATUS, value.getStatus());
        jgen.writeStringField(Constants.CLIENTE_PESSOA_FISICA_RG, value.getRg());
        jgen.writeStringField(Constants.CLIENTE_PESSOA_FISICA_CPF, value.getCpf());
        jgen.writeEndObject();
    }

    public static String serializeClientePessoaFisica(ClientePessoaFisica clientePessoaFisica) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ClientePessoaFisica.class, new ClientePessoaFisicaSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsString(clientePessoaFisica);
    }    
}