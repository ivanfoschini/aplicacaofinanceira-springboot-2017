package aplicacaofinanceira.util;

import aplicacaofinanceira.model.ClientePessoaJuridica;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ClientePessoaJuridicaSerializer extends StdSerializer<ClientePessoaJuridica> {

    public ClientePessoaJuridicaSerializer() {
        this(null);
    }

    public ClientePessoaJuridicaSerializer(Class<ClientePessoaJuridica> t) {
        super(t);
    }

    @Override
    public void serialize(ClientePessoaJuridica value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField(Constants.CLIENTE_ID, value.getId());
        jgen.writeStringField(Constants.CLIENTE_NOME, value.getNome());
        jgen.writeStringField(Constants.CLIENTE_STATUS, value.getStatus());
        jgen.writeStringField(Constants.CLIENTE_PESSOA_JURIDICA_CNPJ, value.getCnpj());
        jgen.writeEndObject();
    }

    public static String serializeClientePessoaJuridica(ClientePessoaJuridica clientePessoaJuridica) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ClientePessoaJuridica.class, new ClientePessoaJuridicaSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsString(clientePessoaJuridica);
    }    
}