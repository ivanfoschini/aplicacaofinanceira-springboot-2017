package aplicacaofinanceira.conta;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.deserializer.ContaCorrenteDeserializer;
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
public class FindOneContaCorrenteIntegrationTest extends BaseIntegrationTest {

    private String getUri = ContaCorrenteTestUtil.CONTAS_CORRENTES_URI + TestUtil.ID_COMPLEMENT_URI;
    private String postUri = ContaCorrenteTestUtil.CONTAS_CORRENTES_URI;
    
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
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFindOneComUsuarioNaoAutorizado() throws Exception {
        String inputJson = createContaCorrenteUmValida();
        
        MvcResult postResult = mockMvc
                .perform(MockMvcRequestBuilders.post(postUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        String content = postResult.getResponse().getContentAsString();        

        ContaCorrenteWithAgenciaDeserializer contaCorrenteWithAgenciaDeserializer = super.mapFromJsonObject(content, ContaCorrenteWithAgenciaDeserializer.class);
        
        Long id = contaCorrenteWithAgenciaDeserializer.getContaId();
        
        MvcResult getResult = mockMvc
                .perform(MockMvcRequestBuilders.get(getUri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getClienteAuthorization()))                
                .andReturn();

        int status = getResult.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindAllComUsuarioComCredenciaisIncorretas() throws Exception {
        String inputJson = createContaCorrenteUmValida();
        
        MvcResult postResult = mockMvc
                .perform(MockMvcRequestBuilders.post(postUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        String content = postResult.getResponse().getContentAsString();        

        ContaCorrenteWithAgenciaDeserializer contaCorrenteWithAgenciaDeserializer = super.mapFromJsonObject(content, ContaCorrenteWithAgenciaDeserializer.class);
        
        Long id = contaCorrenteWithAgenciaDeserializer.getContaId();
        
        MvcResult getResult = mockMvc
                .perform(MockMvcRequestBuilders.get(getUri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = getResult.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindOneComContaCorrenteInexistente() throws Exception {
        String inputJson = createContaCorrenteUmValida();
        
        mockMvc
                .perform(MockMvcRequestBuilders.post(postUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        MvcResult getResult = mockMvc
                .perform(MockMvcRequestBuilders.get(getUri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = getResult.getResponse().getStatus();
        String content = getResult.getResponse().getContentAsString(); 
        
        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("contaNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    } 
    
    @Test
    public void testFindOneComSucesso() throws Exception {
        String inputJson = createContaCorrenteUmValida();
        
        MvcResult postResult = mockMvc
                .perform(MockMvcRequestBuilders.post(postUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        String postContent = postResult.getResponse().getContentAsString();        

        ContaCorrenteWithAgenciaDeserializer contaCorrenteWithAgenciaDeserializer = super.mapFromJsonObject(postContent, ContaCorrenteWithAgenciaDeserializer.class);
        
        Long id = contaCorrenteWithAgenciaDeserializer.getContaId();
        
        MvcResult getResult = mockMvc
                .perform(MockMvcRequestBuilders.get(getUri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = getResult.getResponse().getStatus();
        String getContent = getResult.getResponse().getContentAsString();        

        ContaCorrenteDeserializer contaCorrenteDeserializer = super.mapFromJsonObject(getContent, ContaCorrenteDeserializer.class);        
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(id, contaCorrenteDeserializer.getId());
        Assert.assertEquals(ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM, contaCorrenteDeserializer.getNumero());
        Assert.assertEquals(Float.valueOf(ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM), Float.valueOf(contaCorrenteDeserializer.getSaldo()));
        Assert.assertEquals(ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM, contaCorrenteDeserializer.getDataDeAbertura());
        Assert.assertEquals(Float.valueOf(ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM), Float.valueOf(contaCorrenteDeserializer.getLimite()));
    }
    
    private String createContaCorrenteUmValida() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaBairro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_UM + ", \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_UM + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_UM + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_UM + ", \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }
}