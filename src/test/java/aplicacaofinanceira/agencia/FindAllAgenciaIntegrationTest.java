package aplicacaofinanceira.agencia;

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
public class FindAllAgenciaIntegrationTest extends BaseIntegrationTest {

    private String uri = AgenciaTestUtil.AGENCIAS_URI;
    
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
        Agencia agencia = createValidAgenciaCentro();
        
        agenciaRepository.save(agencia);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }    
    
    @Test
    public void testFindAllComUsuarioComCredenciaisIncorretas() throws Exception {
        Agencia agencia = createValidAgenciaCentro();
        
        agenciaRepository.save(agencia);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindAllComSucesso() throws Exception {        
        Agencia agenciaBairro = createValidAgenciaBairro();
        Agencia agenciaCampus = createValidAgenciaCampus();
        Agencia agenciaCentro = createValidAgenciaCentro();
        
        agenciaRepository.save(agenciaBairro);
        agenciaRepository.save(agenciaCampus);
        agenciaRepository.save(agenciaCentro);
        
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
    
    private Agencia createValidAgenciaBairro() {
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

        return agencia;
    }
    
    private Agencia createValidAgenciaCampus() {
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

        return agencia;
    }
    
    private Agencia createValidAgenciaCentro() {
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

        return agencia;
    }
}