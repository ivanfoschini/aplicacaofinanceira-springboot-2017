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
public class UpdateCidadeIntegrationTest extends BaseIntegrationTest {

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
    public void testUpdateComUsuarioNaoAutorizado() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        String inputJson = super.mapToJson(cidade);

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
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        String inputJson = super.mapToJson(cidade);

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
    public void testUpdateComSucesso() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade saoCarlos = CidadeTestUtil.saoCarlos();
        saoCarlos.setEstado(estado);
        
        cidadeRepository.save(saoCarlos);
        
        Long id = saoCarlos.getId();
        
        Cidade aracatuba = CidadeTestUtil.aracatuba();
        aracatuba.setEstado(estado);
        
        String inputJson = super.mapToJson(aracatuba);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        CidadeWithEstado cidadeWithEstado = super.mapFromJsonObject(content, CidadeWithEstado.class);                
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertNotNull(cidadeWithEstado.getCidadeId());
        Assert.assertEquals(aracatuba.getNome(), cidadeWithEstado.getCidadeNome());
        Assert.assertEquals(aracatuba.getEstado().getId(), cidadeWithEstado.getEstadoId());
        Assert.assertEquals(aracatuba.getEstado().getNome(), cidadeWithEstado.getEstadoNome());
    }  
    
    @Test
    public void testUpdateSemCamposObrigatorios() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        Cidade cidadeSemCamposObrigatorios = CidadeTestUtil.cidadeSemCamposObrigatorios();
        
        String inputJson = super.mapToJson(cidadeSemCamposObrigatorios);

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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeNaoPodeSerNulo", null, null), errorResponse.getMessages().get(0));
    }

    @Test
    public void testUpdateComEstadoInvalido() throws Exception {
        Estado saoPaulo = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(saoPaulo);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(saoPaulo);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        Estado estadoInvalido = new Estado();
        estadoInvalido.setId(new Long(0));
        
        cidade.setEstado(estadoInvalido);   
        
        String inputJson = super.mapToJson(cidade);

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
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("estadoNaoEncontrado", null, null), errorResponse.getMessage());
    }    
    
    @Test
    public void testUpdateComCidadeCujoNomeJaCorrespondeAoNomeDeOutraCidadeDoEstado() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);        
        
        Cidade saoCarlos = CidadeTestUtil.saoCarlos();
        saoCarlos.setEstado(estado);
        
        Cidade aracatuba = CidadeTestUtil.aracatuba();
        aracatuba.setEstado(estado);
        
        cidadeRepository.save(saoCarlos);
        cidadeRepository.save(aracatuba);
        
        aracatuba.setNome(saoCarlos.getNome());
        
        Long id = aracatuba.getId();
        
        String inputJson = super.mapToJson(aracatuba);

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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null), errorResponse.getMessage());
    }  
    
    @Test
    public void testUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        Cidade cidadeComNomeComMenosDeDoisCaracteres = CidadeTestUtil.cidadeComNomeComMenosDeDoisCaracteres();
        
        String inputJson = super.mapToJson(cidadeComNomeComMenosDeDoisCaracteres);

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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        Cidade cidadeComNomeComMaisDeDuzentosECinquentaECincoCaracteres = CidadeTestUtil.cidadeComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        
        String inputJson = super.mapToJson(cidadeComNomeComMaisDeDuzentosECinquentaECincoCaracteres);

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
        Assert.assertEquals(messageSource.getMessage("cidadeNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComCidadeInexistente() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        String inputJson = super.mapToJson(cidade);

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
        Assert.assertEquals(messageSource.getMessage("cidadeNaoEncontrada", null, null), errorResponse.getMessage());
    }  
}