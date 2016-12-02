package aplicacaofinanceira.banco;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
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
public class InsertBancoIntegrationTest extends BaseIntegrationTest {

    private String uri = BancoTestUtil.BANCOS_URI;
    
    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testSaveComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String inputJson = super.mapToJson(banco);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }

    @Test
    public void testSaveComUsuarioComCredenciaisIncorretas() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        String inputJson = super.mapToJson(banco);

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
    public void testSaveSemCamposObrigatorios() throws Exception {
        Banco banco = BancoTestUtil.bancoSemCamposObrigatorios();
        
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

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoCnpjNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(0));
        Assert.assertEquals(messageSource.getMessage("bancoCnpjInvalido", null, null), errorResponseDeserializer.getMessages().get(1));
        Assert.assertEquals(messageSource.getMessage("bancoNomeNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(2));
    }
    
    @Test
    public void testSaveComNumeroMenorDoQueUm() throws Exception {
        Banco banco = BancoTestUtil.bancoComNumeroMenorDoQueUm();
        
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

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testSaveComNumeroDuplicado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
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

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.NOT_UNIQUE_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testSaveComCnpjInvalido() throws Exception {
        Banco banco = BancoTestUtil.bancoComCnpjInvalido();
        
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

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoCnpjInvalido", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testSaveComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco banco = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();
        
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

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testSaveComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco banco = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
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

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testSaveComSucesso() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
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

        Banco savedBanco = super.mapFromJsonObject(content, Banco.class);        
        banco.setId(savedBanco.getId());
        
        Assert.assertEquals(HttpStatus.CREATED.value(), status);
        Assert.assertEquals(banco, savedBanco);
    }
}