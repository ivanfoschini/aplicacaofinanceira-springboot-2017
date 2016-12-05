package aplicacaofinanceira.conta;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
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
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FindAllContaCorrenteIntegrationTest extends BaseIntegrationTest {

    private String uri = ContaCorrenteTestUtil.CONTAS_CORRENTES_URI;
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFindAllComUsuarioNaoAutorizado() throws Exception {
        String inputJson = createContaCorrenteUmValida();

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
    public void testFindAllComUsuarioComCredenciaisIncorretas() throws Exception {
        String inputJson = createContaCorrenteUmValida();

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
    public void testFindAllComSucesso() throws Exception {
        String inputJsonContaCorrenteUm = createContaCorrenteUmValida();
        String inputJsonContaCorrenteDois = createContaCorrenteDoisValida();
        String inputJsonContaCorrenteTres = createContaCorrenteTresValida();
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJsonContaCorrenteUm))                
                .andReturn();
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJsonContaCorrenteDois))                
                .andReturn();
        
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJsonContaCorrenteTres))                
                .andReturn();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        
        List<Object> listSuccessResponse = super.mapFromJsonArray(content);        
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertTrue(listSuccessResponse.size() == TestUtil.DEFAULT_SUCCESS_LIST_SIZE);        
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
    
    private String createContaCorrenteDoisValida() {
        Estado estado = EstadoTestUtil.rioDeJaneiro();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.rioDeJaneiro();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.caixaEconomicaFederal();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaCampus();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_DOIS + ", \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_DOIS + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_DOIS + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_DOIS + ", \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }
    
    private String createContaCorrenteTresValida() {
        Estado estado = EstadoTestUtil.minasGerais();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.uberlandia();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.itau();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        agenciaRepository.save(agencia);        
        
        return "{ \"numero\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_NUMERO_TRES + ", \"dataDeAbertura\": \"" + ContaCorrenteTestUtil.CONTA_CORRENTE_DATA_DE_ABERTURA_TRES + "\", \"saldo\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_SALDO_TRES + ", \"limite\": " + ContaCorrenteTestUtil.CONTA_CORRENTE_LIMITE_TRES + ", \"agencia\": { \"id\": " + agencia.getId() + " } }";
    }
}