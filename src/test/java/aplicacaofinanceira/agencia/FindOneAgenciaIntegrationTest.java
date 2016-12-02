package aplicacaofinanceira.agencia;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
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
public class FindOneAgenciaIntegrationTest extends BaseIntegrationTest {

    private String uri = AgenciaTestUtil.AGENCIAS_URI + TestUtil.ID_COMPLEMENT_URI;
    
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
        Agencia agencia = createValidAgencia();
        
        agenciaRepository.save(agencia);
        
        Long id = agencia.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }
    
    @Test
    public void testFindAllComUsuarioComCredenciaisIncorretas() throws Exception {
        Agencia agencia = createValidAgencia();
        
        agenciaRepository.save(agencia);
        
        Long id = agencia.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindOneComAgenciaInexistente() throws Exception {
        Agencia agencia = createValidAgencia();
        
        agenciaRepository.save(agencia);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("agenciaNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    } 
    
    @Test
    public void testFindOneComSucesso() throws Exception {
        Agencia agencia = createValidAgencia();
        
        agenciaRepository.save(agencia);
        
        Long id = agencia.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Agencia savedAgencia = super.mapFromJsonObject(content, Agencia.class);        
        agencia.setId(savedAgencia.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(agencia, savedAgencia);
    }
    
    private Agencia createValidAgencia() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
}