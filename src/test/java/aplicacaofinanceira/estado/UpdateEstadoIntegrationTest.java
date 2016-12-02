package aplicacaofinanceira.estado;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
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
public class UpdateEstadoIntegrationTest extends BaseIntegrationTest {

    private String uri = EstadoTestUtil.ESTADOS_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;
    
    @Before
    public void setUp() {
        super.setUp();
    }
    
    @Test
    public void testUpdateComUsuarioNaoAutorizado() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        Long id = saoPaulo.getId();
        
        String inputJson = super.mapToJson(saoPaulo);

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
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        Long id = saoPaulo.getId();
        
        String inputJson = super.mapToJson(saoPaulo);

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
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        Long id = saoPaulo.getId();
        
        Estado estadoSemCamposObrigatorios = EstadoTestUtil.estadoSemCamposObrigatorios();
        
        String inputJson = super.mapToJson(estadoSemCamposObrigatorios);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
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
        Assert.assertEquals(messageSource.getMessage("estadoNomeNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeDuplicado() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        Estado rioDeJaneiro = EstadoTestUtil.rioDeJaneiro();        
        
        estadoRepository.save(saoPaulo);        
        estadoRepository.save(rioDeJaneiro);
        
        Long id = rioDeJaneiro.getId();
        
        String inputJson = super.mapToJson(saoPaulo);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
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
        Assert.assertEquals(messageSource.getMessage("estadoNomeDeveSerUnico", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        Long id = saoPaulo.getId();
        
        Estado estadoComNomeComMenosDeDoisCaracteres = EstadoTestUtil.estadoComNomeComMenosDeDoisCaracteres();
        
        String inputJson = super.mapToJson(estadoComNomeComMenosDeDoisCaracteres);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
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
        Assert.assertEquals(messageSource.getMessage("estadoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        Long id = saoPaulo.getId();
        
        Estado estadoComNomeComMaisDeDuzentosECinquentaECincoCaracteres = EstadoTestUtil.estadoComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = super.mapToJson(estadoComNomeComMaisDeDuzentosECinquentaECincoCaracteres);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
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
        Assert.assertEquals(messageSource.getMessage("estadoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComEstadoInexistente() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        String inputJson = super.mapToJson(saoPaulo);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("estadoNaoEncontrado", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testUpdateComSucesso() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(saoPaulo);
        
        Long id = saoPaulo.getId();
        
        Estado rioDeJaneiro = EstadoTestUtil.rioDeJaneiro();
        
        String inputJson = super.mapToJson(rioDeJaneiro);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        Estado updatedEstado = super.mapFromJsonObject(content, Estado.class);        
        rioDeJaneiro.setId(updatedEstado.getId());
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertEquals(rioDeJaneiro, updatedEstado);
    }    
}