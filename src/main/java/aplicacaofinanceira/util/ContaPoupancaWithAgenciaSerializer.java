package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.ContaPoupanca;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class ContaPoupancaWithAgenciaSerializer extends StdSerializer<ContaPoupanca> {

    public ContaPoupancaWithAgenciaSerializer() {
        this(null);
    }

    public ContaPoupancaWithAgenciaSerializer(Class<ContaPoupanca> t) {
        super(t);
    }

    @Override
    public void serialize(ContaPoupanca value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeNumberField(Constants.CONTA_ID, value.getId());
        jgen.writeNumberField(Constants.CONTA_NUMERO, value.getNumero());
        jgen.writeStringField(Constants.CONTA_DATA_DE_ABERTURA, DateUtil.dateToString(value.getDataDeAbertura().toDate()));
        jgen.writeNumberField(Constants.CONTA_SALDO, value.getSaldo());
        jgen.writeStringField(Constants.CONTA_POUPANCA_DATA_DE_ANIVERSARIO, DateUtil.dateToString(value.getDataDeAniversario().toDate()));
        jgen.writeNumberField(Constants.CONTA_POUPANCA_CORRECAO_MONETARIA, value.getCorrecaoMonetaria());
        jgen.writeNumberField(Constants.CONTA_POUPANCA_JUROS, value.getJuros());
        jgen.writeNumberField(Constants.AGENCIA_ID, value.getAgencia().getId());
        jgen.writeStringField(Constants.AGENCIA_NOME, value.getAgencia().getNome());
        jgen.writeEndObject();
    }

    public static String serializeContaPoupancaWithAgencia(ContaPoupanca contaPoupanca, Agencia agencia) throws JsonProcessingException {
        contaPoupanca.getAgencia().setNome(agencia.getNome());

        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ContaPoupanca.class, new ContaPoupancaWithAgenciaSerializer());
        mapper.registerModule(module);

        return mapper.writeValueAsString(contaPoupanca);
    }
}
