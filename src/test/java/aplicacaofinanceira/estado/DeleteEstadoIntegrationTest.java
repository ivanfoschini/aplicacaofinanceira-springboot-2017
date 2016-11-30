package aplicacaofinanceira.estado;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.ErrorResponse;
import aplicacaofinanceira.util.EstadoTestUtil;
import aplicacaofinanceira.util.EstadoWithCidade;
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
public class DeleteEstadoIntegrationTest extends BaseIntegrationTest {
 
    private String uri = EstadoTestUtil.ESTADOS_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testDeleteComUsuarioNaoAutorizado() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);
        
        Long id = estado.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }
    
    @Test
    public void testDeleteComUsuarioComCredenciaisIncorretas() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);
        
        Long id = estado.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }  
    
    @Test
    public void testDeleteComEstadoInexistente() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("estadoNaoEncontrado", null, null), errorResponse.getMessage());
    } 
    
    @Test
    public void testDeleteComEstadoQuePossuiPeloMenosUmaCidadeAssociada() throws Exception {
        EstadoWithCidade estadoWithCidade = createEstadoWithCidade();
        
        Estado estado = estadoWithCidade.getEstado();
        
        Long id = estado.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.NOT_EMPTY_COLLECTION_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("estadoPossuiCidades", null, null), errorResponse.getMessage());
    } 

    @Test
    public void testDeleteComSucesso() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();
        estado.setCidades(new ArrayList<>());
        
        estadoRepository.save(estado);
        
        Long id = estado.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), status);        
    } 
    
    private EstadoWithCidade createEstadoWithCidade() {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        estado.setCidades(new ArrayList<>());
        estado.getCidades().add(cidade);
        
        cidadeRepository.save(cidade);
        
        EstadoWithCidade estadoWithCidade = new EstadoWithCidade(cidade, estado);
        
        return estadoWithCidade;
    }    
}