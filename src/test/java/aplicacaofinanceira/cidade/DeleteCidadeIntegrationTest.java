package aplicacaofinanceira.cidade;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.EnderecoRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.EnderecoTestUtil;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
import aplicacaofinanceira.util.EstadoTestUtil;
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
public class DeleteCidadeIntegrationTest extends BaseIntegrationTest {
 
    private String uri = CidadeTestUtil.CIDADES_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;
    
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
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
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
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorizationWithWrongPassword()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.UNAUTHORIZED.value(), status);
    }  
    
    @Test
    public void testDeleteComCidadeInexistente() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        
        cidadeRepository.save(cidade);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("cidadeNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    } 
    
    @Test
    public void testDeleteComCidadeQuePossuiPeloMenosUmEnderecoAssociado() throws Exception {
        Cidade cidade = createCidadeWithEndereco();
        
        Long id = cidade.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponseDeserializer errorResponseDeserializer = super.mapFromJsonObject(content, ErrorResponseDeserializer.class);
        
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
        Assert.assertEquals(TestUtil.NOT_EMPTY_COLLECTION_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("cidadePossuiEnderecos", null, null), errorResponseDeserializer.getMessage());
    } 

    @Test
    public void testDeleteComSucesso() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();        
        
        estadoRepository.save(estado);
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);
        cidade.setEnderecos(new ArrayList<>());
        
        cidadeRepository.save(cidade);
        
        Long id = cidade.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), status);        
    } 
    
    private Cidade createCidadeWithEndereco() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        cidade.setEnderecos(new ArrayList<>());
        cidade.getEnderecos().add(endereco);

        enderecoRepository.save(endereco);
        
        return cidade;
    }    
}