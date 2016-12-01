package aplicacaofinanceira.cidade;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.CidadeWithEstado;
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
public class InsertCidadeIntegrationTest extends BaseIntegrationTest {

    private String uri = CidadeTestUtil.CIDADES_URI;
    
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
    public void testSaveComUsuarioNaoAutorizado() throws Exception {
        Cidade cidade = CidadeTestUtil.saoCarlos();
        
        String inputJson = super.mapToJson(cidade);

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
        Cidade cidade = CidadeTestUtil.saoCarlos();
        
        String inputJson = super.mapToJson(cidade);

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
    public void testSaveComSucesso() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        String inputJson = super.mapToJson(cidade);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        CidadeWithEstado cidadeWithEstado = super.mapFromJsonObject(content, CidadeWithEstado.class);                
        
        Assert.assertEquals(HttpStatus.CREATED.value(), status);
        Assert.assertNotNull(cidadeWithEstado.getCidadeId());
        Assert.assertEquals(cidade.getNome(), cidadeWithEstado.getCidadeNome());
        Assert.assertEquals(cidade.getEstado().getId(), cidadeWithEstado.getEstadoId());
        Assert.assertEquals(cidade.getEstado().getNome(), cidadeWithEstado.getEstadoNome());
    }  
    
    @Test
    public void testSaveSemCamposObrigatorios() throws Exception {
        Cidade cidade = CidadeTestUtil.cidadeSemCamposObrigatorios();
        
        String inputJson = super.mapToJson(cidade);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeNaoPodeSerNulo", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testSaveComEstadoInvalido() throws Exception {
        Estado estado = new Estado();
        estado.setId(new Long(0));
        
        Cidade cidade = CidadeTestUtil.saoCarlos();        
        cidade.setEstado(estado);   
        
        String inputJson = super.mapToJson(cidade);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
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
        Assert.assertEquals(messageSource.getMessage("estadoNaoEncontrado", null, null), errorResponse.getMessage());
    }
    
    @Test
    public void testSaveComCidadeCujoNomeJaCorrespondeAoNomeDeOutraCidadeDoEstado() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        String inputJson = super.mapToJson(cidade);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null), errorResponse.getMessage());
    }  
    
    @Test
    public void testSaveComNomeComMenosDeDoisCaracteres() throws Exception {
        Cidade cidade = CidadeTestUtil.cidadeComNomeComMenosDeDoisCaracteres();
        
        String inputJson = super.mapToJson(cidade);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testSaveComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Cidade cidade = CidadeTestUtil.cidadeComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = super.mapToJson(cidade);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
    } 
}