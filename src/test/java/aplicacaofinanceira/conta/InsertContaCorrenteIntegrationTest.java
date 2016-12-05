package aplicacaofinanceira.conta;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.deserializer.ContaCorrenteWithAgenciaDeserializer;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.ContaCorrente;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.AgenciaRepository;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.ContaCorrenteRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.AgenciaTestUtil;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.ContaCorrenteTestUtil;
import aplicacaofinanceira.util.EnderecoTestUtil;
import aplicacaofinanceira.util.EstadoTestUtil;
import aplicacaofinanceira.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class InsertContaCorrenteIntegrationTest extends BaseIntegrationTest {

    private String uri = ContaCorrenteTestUtil.CONTAS_CORRENTES_URI;
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    private Agencia agencia;

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testSaveComUsuarioNaoAutorizado() throws Exception {
        String inputJson = createValidContaCorrente();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getClienteAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }

    @Test
    public void testSaveComUsuarioComCredenciaisIncorretas() throws Exception {
        String inputJson = createValidContaCorrente();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
        
    @Test
    public void testSaveComSucesso() throws Exception {
        String inputJson = createValidContaCorrente();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ContaCorrenteWithAgenciaDeserializer contaCorrenteWithAgenciaDeserializer = super.mapFromJsonObject(content, ContaCorrenteWithAgenciaDeserializer.class);
        
        Assert.assertEquals(HttpStatus.CREATED.value(), status);
        Assert.assertNotNull(contaCorrenteWithAgenciaDeserializer.getContaId());
        Assert.assertEquals(ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM, contaCorrenteWithAgenciaDeserializer.getContaNumero());
        Assert.assertEquals(ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM, contaCorrenteWithAgenciaDeserializer.getContaDataDeAbertura());      
        Assert.assertEquals(ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM, contaCorrenteWithAgenciaDeserializer.getContaSaldo(), 0.0f);
        Assert.assertEquals(ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM, contaCorrenteWithAgenciaDeserializer.getContaLimite(), 0.0f);
        Assert.assertEquals(agencia.getId(), contaCorrenteWithAgenciaDeserializer.getAgenciaId());
        Assert.assertEquals(agencia.getNome(), contaCorrenteWithAgenciaDeserializer.getAgenciaNome());
    }  
    
    @Test
    public void testSaveComAgenciaInvalida() throws Exception {
        String inputJson = createContaCorrenteWithAgenciaInvalid();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("agenciaNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    }    
    
    @Test
    public void testSaveSemCamposObrigatorios() throws Exception {
        String inputJson = createContaCorrenteWithoutRequiredFields();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("contaDataDeAberturaNaoPodeSerNula", null, null), errorResponseDeserializer.getMessages().get(0));
        Assert.assertEquals(messageSource.getMessage("contaNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(1));
    }
    
    @Test
    public void testSaveComNumeroMenorDoQueUm() throws Exception {
        String inputJson = createContaCorrenteWithNumberLessThanOne();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("contaNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testSaveComNumeroDuplicado() throws Exception {
        String inputJson = createValidContaCorrente();

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.NOT_UNIQUE_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("contaNumeroDeveSerUnico", null, null), errorResponseDeserializer.getMessage());
    }    
    
    @Test
    public void testSaveComDataInvalida() throws Exception {
        String inputJson = createContaCorrenteWithInvalidDate();

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
        Assert.assertEquals(TestUtil.HTTP_NOT_READABLE_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("generalBadRequest", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testSaveComLimiteMenorDoQueZero() throws Exception {
        String inputJson = createContaCorrenteWithLimiteLessThanZero();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("contaCorrenteLimiteDeveSerMaiorOuIgualAZero", null, null), errorResponseDeserializer.getMessages().get(0));
    } 
    
    private String createValidContaCorrente() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM + ", \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM + ", \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }

    private String createContaCorrenteWithAgenciaInvalid() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM + ", \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM + ", \"agencia\": { \"id\": 0 } }";
    }

    private String createContaCorrenteWithoutRequiredFields() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": null, \"dataDeAbertura\": null, \"saldo\": null, \"limite\": null, \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }

    private String createContaCorrenteWithNumberLessThanOne() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": 0, \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM + ", \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }

    private String createContaCorrenteWithInvalidDate() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM + ", \"dataDeAbertura\": \"2017-02-30 \", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM + ", \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }

    private String createContaCorrenteWithLimiteLessThanZero() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM + ", \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM + ", \"limite\": -1, \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }
}