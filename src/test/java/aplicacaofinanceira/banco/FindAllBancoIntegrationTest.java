package aplicacaofinanceira.banco;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.TestUtil;
import java.util.List;
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

public class FindAllBancoIntegrationTest extends BaseIntegrationTest {

    private String uri = BancoTestUtil.BANCOS_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testFindAllComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
        
        bancoRepository.delete(banco);
    }    
    
    @Test
    public void testFindAllComUsuarioComCredenciaisIncorretas() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
        
        bancoRepository.delete(banco);
    }
    
    @Test
    public void testFindAllComSucesso() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        Banco itau = BancoTestUtil.itau();
        
        bancoRepository.save(bancoDoBrasil);
        bancoRepository.save(caixaEconomicaFederal);
        bancoRepository.save(itau);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        
        List<Object> listSuccessResponse = super.mapFromJsonArray(content);
        
        bancoRepository.delete(bancoDoBrasil);
        bancoRepository.delete(caixaEconomicaFederal);
        bancoRepository.delete(itau);
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertTrue(listSuccessResponse.size() == 3);        
    }
}