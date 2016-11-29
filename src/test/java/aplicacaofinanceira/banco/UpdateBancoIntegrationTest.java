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
public class UpdateBancoIntegrationTest extends BaseIntegrationTest {

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
    public void testUpdateComUsuarioNaoAutorizado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
    }

    @Test
    public void testUpdateComUsuarioComCredenciaisIncorretas() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }  
    
    @Test
    public void testUpdateSemCamposObrigatorios() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoSemCamposObrigatorios = BancoTestUtil.bancoSemCamposObrigatorios();
        
        String inputJson = super.mapToJson(bancoSemCamposObrigatorios);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoCnpjNaoPodeSerNulo", null, null), errorResponse.getMessages().get(0));
        Assert.assertEquals(messageSource.getMessage("bancoCnpjInvalido", null, null), errorResponse.getMessages().get(1));
        Assert.assertEquals(messageSource.getMessage("bancoNomeNaoPodeSerNulo", null, null), errorResponse.getMessages().get(2));
        Assert.assertEquals(messageSource.getMessage("bancoNumeroNaoPodeSerNulo", null, null), errorResponse.getMessages().get(3));
    }
    
    @Test
    public void testUpdateComNumeroMenorDoQueUm() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNumeroMenorDoQueUm = BancoTestUtil.bancoComNumeroMenorDoQueUm();
        
        String inputJson = super.mapToJson(bancoComNumeroMenorDoQueUm);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNumeroDeveSerMaiorDoQueZero", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNumeroDuplicado() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();        
        
        bancoRepository.save(bancoDoBrasil);        
        bancoRepository.save(caixaEconomicaFederal);
        
        Long id = caixaEconomicaFederal.getId();
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.NOT_UNIQUE_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null), errorResponse.getMessage());
    }
    
    @Test
    public void testUpdateComCnpjInvalido() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoComCnpjInvalido();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComCnpjInvalido = BancoTestUtil.bancoComCnpjInvalido();
        
        String inputJson = super.mapToJson(bancoComCnpjInvalido);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoCnpjInvalido", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNomeComMenosDeDoisCaracteres = BancoTestUtil.bancoComNomeComMenosDeDoisCaracteres();
        
        String inputJson = super.mapToJson(bancoComNomeComMenosDeDoisCaracteres);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres = BancoTestUtil.bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = super.mapToJson(bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComBancoInexistente() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        String inputJson = super.mapToJson(bancoDoBrasil);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNaoEncontrado", null, null), errorResponse.getMessage());
    }
    
    @Test
    public void testUpdateComSucesso() throws Exception {
        Banco bancoDoBrasil = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(bancoDoBrasil);
        
        Long id = bancoDoBrasil.getId();
        
        Banco caixaEconomicaFederal = BancoTestUtil.caixaEconomicaFederal();
        
        String inputJson = super.mapToJson(caixaEconomicaFederal);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Banco updatedBanco = super.mapFromJsonObject(content, Banco.class);        
        caixaEconomicaFederal.setId(updatedBanco.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(caixaEconomicaFederal, updatedBanco);
    }    
}