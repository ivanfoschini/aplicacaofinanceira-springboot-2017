package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class AgenciaWithEnderecoAndBancoSerializer extends StdSerializer<Agencia> {

    public AgenciaWithEnderecoAndBancoSerializer() {
        this(null);
    }

    public AgenciaWithEnderecoAndBancoSerializer(Class<Agencia> t) {
        super(t);
    }

    @Override
    public void serialize(Agencia value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField(Constants.AGENCIA_ID, value.getId());
        jgen.writeStringField(Constants.AGENCIA_NOME, value.getNome());
        jgen.writeNumberField(Constants.AGENCIA_NUMERO, value.getNumero());
        jgen.writeNumberField(Constants.ENDERECO_ID, value.getBanco().getId());
        jgen.writeStringField(Constants.ENDERECO_LOGRADOURO, value.getEndereco().getLogradouro());
        jgen.writeNumberField(Constants.ENDERECO_NUMERO, value.getEndereco().getNumero());
        jgen.writeStringField(Constants.ENDERECO_COMPLEMENTO, value.getEndereco().getComplemento());
        jgen.writeStringField(Constants.ENDERECO_BAIRRO, value.getEndereco().getBairro());
        jgen.writeStringField(Constants.ENDERECO_CEP, value.getEndereco().getCep());
        jgen.writeNumberField(Constants.CIDADE_ID, value.getEndereco().getCidade().getId());
        jgen.writeStringField(Constants.CIDADE_NOME, value.getEndereco().getCidade().getNome());
        jgen.writeNumberField(Constants.ESTADO_ID, value.getEndereco().getCidade().getEstado().getId());
        jgen.writeStringField(Constants.ESTADO_NOME, value.getEndereco().getCidade().getEstado().getNome());
        jgen.writeNumberField(Constants.BANCO_ID, value.getBanco().getId());
        jgen.writeStringField(Constants.BANCO_NOME, value.getBanco().getNome());
        jgen.writeEndObject();
    }

    public static String serializeAgenciaWithEnderecoAndBanco(Agencia agencia, Cidade cidade, Estado estado, Banco banco) throws JsonProcessingException {
        agencia.getEndereco().setCidade(cidade);
        agencia.getEndereco().getCidade().setEstado(estado);
        agencia.setBanco(banco);        

        ObjectMapper mapper = new ObjectMapper();
        
        SimpleModule module = new SimpleModule();
        module.addSerializer(Agencia.class, new AgenciaWithEnderecoAndBancoSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsString(agencia);
    }    
}
