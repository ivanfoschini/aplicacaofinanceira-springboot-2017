package aplicacaofinanceira.conta;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.deserializer.ContaCorrenteWithAgenciaDeserializer;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.ClientePessoaFisica;
import aplicacaofinanceira.model.ContaCorrente;
import aplicacaofinanceira.model.Correntista;
import aplicacaofinanceira.model.CorrentistaPK;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.AgenciaRepository;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.ClientePessoaFisicaRepository;
import aplicacaofinanceira.repository.ContaCorrenteRepository;
import aplicacaofinanceira.repository.CorrentistaRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.AgenciaTestUtil;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.ClientePessoaFisicaTestUtil;
import aplicacaofinanceira.util.ContaCorrenteTestUtil;
import aplicacaofinanceira.util.EnderecoTestUtil;
import aplicacaofinanceira.util.EstadoTestUtil;
import aplicacaofinanceira.util.TestUtil;
import java.util.ArrayList;
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
public class DeleteContaCorrenteIntegrationTest extends BaseIntegrationTest {
 
    private String deleteUri = ContaCorrenteTestUtil.CONTAS_CORRENTES_URI + TestUtil.ID_COMPLEMENT_URI;
    private String postUri = ContaCorrenteTestUtil.CONTAS_CORRENTES_URI;
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private ClientePessoaFisicaRepository clientePessoaFisicaRepository;
    
    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;
    
    @Autowired
    private CorrentistaRepository correntistaRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testDeleteComUsuarioNaoAutorizado() throws Exception {
        String inputJson = createContaCorrenteValida();
        
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
        
        MvcResult deleteResult = mockMvc
                .perform(MockMvcRequestBuilders.delete(deleteUri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getClienteAuthorization()))                
                .andReturn();

        int status = deleteResult.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testDeleteComUsuarioComCredenciaisIncorretas() throws Exception {
        String inputJson = createContaCorrenteValida();
        
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
        
        MvcResult deleteResult = mockMvc
                .perform(MockMvcRequestBuilders.delete(deleteUri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = deleteResult.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }  
    
    @Test
    public void testDeleteComContaCorrenteInexistente() throws Exception {
        String inputJson = createContaCorrenteValida();
        
        mockMvc
                .perform(MockMvcRequestBuilders.post(postUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(deleteUri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();                
        
        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("contaNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    } 
    
//    @Test
//    public void testDeleteComContaCorrenteQuePossuiPeloMenosUmaCorrentista() throws Exception {
//        ClientePessoaFisica clientePessoaFisica = createClientePessoaFisicaValido();
//        
//        String inputJsonContaCorrente = createContaCorrenteValida();
//        
//        MvcResult postResult = mockMvc
//                .perform(MockMvcRequestBuilders.post(postUri)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
//                        .content(inputJsonContaCorrente))                
//                .andReturn();
//        
//        String contentPostResult = postResult.getResponse().getContentAsString();        
//
//        ContaCorrenteWithAgenciaDeserializer contaCorrenteWithAgenciaDeserializer = super.mapFromJsonObject(contentPostResult, ContaCorrenteWithAgenciaDeserializer.class);
//        
//        Long clienteId = clientePessoaFisica.getId();
//        Long id = contaCorrenteWithAgenciaDeserializer.getContaId();
//        
//        ContaCorrente contaCorrente = contaCorrenteRepository.findOne(id);        
//        contaCorrente.setCorrentistas(new ArrayList<>());        
//        
//        Correntista correntista = new Correntista(new CorrentistaPK(id, clienteId), true, contaCorrente, clientePessoaFisica); 
//        correntista.setConta(contaCorrente);
//        
//        correntistaRepository.save(correntista);
//        
//        MvcResult deleteResult = mockMvc
//                .perform(MockMvcRequestBuilders.delete(deleteUri, id)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
//                .andReturn();
//
//        int status = deleteResult.getResponse().getStatus();
//        String contentDeleteResult = deleteResult.getResponse().getContentAsString(); 
//        
//        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(contentDeleteResult, ErrorResponseDeserializer.class);
//        
//        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
//        Assert.assertEquals(TestUtil.NOT_EMPTY_COLLECTION_EXCEPTION, errorResponseDeserializer.getException());
//        Assert.assertEquals(messageSource.getMessage("estadoPossuiCidades", null, null), errorResponseDeserializer.getMessage());
//    } 

    @Test
    public void testDeleteComSucesso() throws Exception {
        String inputJsonContaCorrente = createContaCorrenteValida();
        
        MvcResult postResult = mockMvc
                .perform(MockMvcRequestBuilders.post(postUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJsonContaCorrente))                
                .andReturn();
        
        String contentPostResult = postResult.getResponse().getContentAsString();        

        ContaCorrenteWithAgenciaDeserializer contaCorrenteWithAgenciaDeserializer = super.mapFromJsonObject(contentPostResult, ContaCorrenteWithAgenciaDeserializer.class);
        
        Long id = contaCorrenteWithAgenciaDeserializer.getContaId();
        
        ContaCorrente contaCorrente = contaCorrenteRepository.findOne(id);        
        contaCorrente.setCorrentistas(new ArrayList<>());        
        
        MvcResult deleteResult = mockMvc
                .perform(MockMvcRequestBuilders.delete(deleteUri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = deleteResult.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), status);        
    } 
    
    private ClientePessoaFisica createClientePessoaFisicaValido() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        ClientePessoaFisica clientePessoaFisica = ClientePessoaFisicaTestUtil.clientePessoaFisica();
        clientePessoaFisica.setEnderecos(new ArrayList<>());
        clientePessoaFisica.getEnderecos().add(endereco);
        
        clientePessoaFisicaRepository.save(clientePessoaFisica);
        
        return clientePessoaFisica;
    }
    
    private String createContaCorrenteValida() {
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