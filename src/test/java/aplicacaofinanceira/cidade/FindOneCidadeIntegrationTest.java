package aplicacaofinanceira.cidade;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.ErrorResponse;
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
public class FindOneCidadeIntegrationTest extends BaseIntegrationTest {

    private String uri = CidadeTestUtil.CIDADES_URI + TestUtil.ID_COMPLEMENT_URI;
    
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
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
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
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindOneComCidadeInexistente() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("cidadeNaoEncontrada", null, null), errorResponse.getMessage());
    } 
    
    @Test
    public void testFindOneComSucesso() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Cidade savedCidade = super.mapFromJsonObject(content, Cidade.class);        
        cidade.setId(savedCidade.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(cidade, savedCidade);
    }    
}