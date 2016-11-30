package aplicacaofinanceira.estado;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.EstadoRepository;
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
public class FindAllEstadoIntegrationTest extends BaseIntegrationTest {

    private String uri = EstadoTestUtil.ESTADOS_URI;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFindAllComUsuarioNaoAutorizado() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);
        
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
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);
        
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
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        Estado rioDeJaneiro = EstadoTestUtil.rioDeJaneiro();
        Estado minasGerais = EstadoTestUtil.minasGerais();
        
        estadoRepository.save(saoPaulo);
        estadoRepository.save(rioDeJaneiro);
        estadoRepository.save(minasGerais);
        
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
}