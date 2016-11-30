package aplicacaofinanceira.banco;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.ErrorResponse;
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
public class FindOneIntegrationTest extends BaseIntegrationTest {

    private String uri = BancoTestUtil.BANCOS_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFindOneComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
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
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }
    
    @Test
    public void testFindOneComBancoInexistente() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
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
        Assert.assertEquals(messageSource.getMessage("bancoNaoEncontrado", null, null), errorResponse.getMessage());
    } 
    
    @Test
    public void testFindOneComSucesso() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Banco savedBanco = super.mapFromJsonObject(content, Banco.class);        
        banco.setId(savedBanco.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(banco, savedBanco);
    }
}