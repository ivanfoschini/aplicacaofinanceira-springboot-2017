package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.ContaCorrente;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ContaCorrenteWithAgenciaSerializer extends StdSerializer<ContaCorrente> {

    public ContaCorrenteWithAgenciaSerializer() {
        this(null);
    }

    public ContaCorrenteWithAgenciaSerializer(Class<ContaCorrente> t) {
        super(t);
    }

    @Override
    public void serialize(ContaCorrente value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField(Constants.CONTA_ID, value.getId());
        jgen.writeNumberField(Constants.CONTA_NUMERO, value.getNumero());
        jgen.writeStringField(Constants.CONTA_DATA_DE_ABERTURA, value.getDataDeAbertura().toString());
        jgen.writeNumberField(Constants.CONTA_SALDO, value.getSaldo());
        jgen.writeNumberField(Constants.CONTA_CORRENTE_LIMITE, value.getLimite());
        jgen.writeNumberField(Constants.AGENCIA_ID, value.getAgencia().getId());
        jgen.writeStringField(Constants.AGENCIA_NOME, value.getAgencia().getNome());
        jgen.writeEndObject();
    }

    public static String serializeContaCorrenteWithAgencia(ContaCorrente contaCorrente, Agencia agencia) throws JsonProcessingException {
        contaCorrente.getAgencia().setNome(agencia.getNome());

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ContaCorrente.class, new ContaCorrenteWithAgenciaSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsString(contaCorrente);
    }
}