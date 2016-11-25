package aplicacaofinanceira.banco;

import aplicacaofinanceira.AbstractControllerTest;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BancoSaveControllerTest extends AbstractControllerTest {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testSaveComSucesso() throws Exception {
        String uri = TestUtil.BANCOS_URI;
        
        Banco banco = TestUtil.createBanco();
        
        String inputJson = super.mapToJson(banco);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Banco savedBanco = super.mapFromJson(content, Banco.class);        
        banco.setId(savedBanco.getId());
        
        Assert.assertEquals(201, status);
        Assert.assertEquals(banco, savedBanco);
    }    
}