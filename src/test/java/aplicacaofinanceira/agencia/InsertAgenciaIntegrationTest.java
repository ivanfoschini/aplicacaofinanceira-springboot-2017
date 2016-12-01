package aplicacaofinanceira.agencia;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.AgenciaRepository;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.EnderecoRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.AgenciaTestUtil;
import aplicacaofinanceira.util.AgenciaWithEnderecoAndBancoDeserializer;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.EnderecoTestUtil;
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
public class InsertAgenciaIntegrationTest extends BaseIntegrationTest {

    private String uri = AgenciaTestUtil.AGENCIAS_URI;
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
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
    public void testSaveComUsuarioNaoAutorizado() throws Exception {
        Agencia agencia = createAgencia();
        
        String inputJson = super.mapToJson(agencia);

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
        Agencia agencia = createAgencia();
        
        String inputJson = super.mapToJson(agencia);

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
        Agencia agencia = createAgencia();
        
        String inputJson = super.mapToJson(agencia);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        AgenciaWithEnderecoAndBancoDeserializer agenciaWithEnderecoAndBancoDeserializer = super.mapFromJsonObject(content, AgenciaWithEnderecoAndBancoDeserializer.class);
        
        Assert.assertEquals(HttpStatus.CREATED.value(), status);
        Assert.assertNotNull(agenciaWithEnderecoAndBancoDeserializer.getAgenciaId());
        Assert.assertEquals(agencia.getNome(), agenciaWithEnderecoAndBancoDeserializer.getAgenciaNome());
        Assert.assertEquals(agencia.getNumero(), agenciaWithEnderecoAndBancoDeserializer.getAgenciaNumero());
//        Assert.assertEquals(agencia.getEndereco().getId(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoId());
        Assert.assertEquals(agencia.getEndereco().getLogradouro(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoLogradouro());
        Assert.assertEquals(agencia.getEndereco().getNumero(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoNumero());
        Assert.assertEquals(agencia.getEndereco().getComplemento(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoComplemento());
        Assert.assertEquals(agencia.getEndereco().getBairro(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoBairro());
        Assert.assertEquals(agencia.getEndereco().getCep(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoCep());
        Assert.assertEquals(agencia.getEndereco().getCidade().getId(), agenciaWithEnderecoAndBancoDeserializer.getCidadeId());
        Assert.assertEquals(agencia.getEndereco().getCidade().getNome(), agenciaWithEnderecoAndBancoDeserializer.getCidadeNome());
        Assert.assertEquals(agencia.getEndereco().getCidade().getEstado().getId(), agenciaWithEnderecoAndBancoDeserializer.getEstadoId());
        Assert.assertEquals(agencia.getEndereco().getCidade().getEstado().getNome(), agenciaWithEnderecoAndBancoDeserializer.getEstadoNome());
        Assert.assertEquals(agencia.getBanco().getId(), agenciaWithEnderecoAndBancoDeserializer.getBancoId());
        Assert.assertEquals(agencia.getBanco().getNome(), agenciaWithEnderecoAndBancoDeserializer.getBancoNome());
    }   
    
//    @Test
//    public void testSaveSemCamposObrigatorios() throws Exception {
//        Agencia agencia = AgenciaTestUtil.agenciaSemCamposObrigatorios();
//        
//        String inputJson = super.mapToJson(agencia);
//
//        MvcResult result = mockMvc
//                .perform(MockMvcRequestBuilders.post(uri)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
//                        .content(inputJson))                
//                .andReturn();
//
//        int status = result.getResponse().getStatus();
//        String content = result.getResponse().getContentAsString();        
//
//        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
//        
//        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
//        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
//        Assert.assertEquals(messageSource.getMessage("agenciaNomeNaoPodeSerNulo", null, null), errorResponse.getMessages().get(0));
//    }
//    
//    @Test
//    public void testSaveComNomeDuplicado() throws Exception {
//        Agencia agencia = AgenciaTestUtil.saoPaulo();
//        
//        agenciaRepository.save(agencia);
//        
//        String inputJson = super.mapToJson(agencia);
//
//        MvcResult result = mockMvc
//                .perform(MockMvcRequestBuilders.post(uri)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
//                        .content(inputJson))                
//                .andReturn();
//
//        int status = result.getResponse().getStatus();
//        String content = result.getResponse().getContentAsString();        
//
//        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
//        
//        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
//        Assert.assertEquals(TestUtil.NOT_UNIQUE_EXCEPTION, errorResponse.getException());
//        Assert.assertEquals(messageSource.getMessage("agenciaNomeDeveSerUnico", null, null), errorResponse.getMessage());
//    }    
//    
//    @Test
//    public void testSaveComNomeComMenosDeDoisCaracteres() throws Exception {
//        Agencia agencia = AgenciaTestUtil.agenciaComNomeComMenosDeDoisCaracteres();
//        
//        String inputJson = super.mapToJson(agencia);
//
//        MvcResult result = mockMvc
//                .perform(MockMvcRequestBuilders.post(uri)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
//                        .content(inputJson))                
//                .andReturn();
//
//        int status = result.getResponse().getStatus();
//        String content = result.getResponse().getContentAsString();        
//
//        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
//        
//        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
//        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
//        Assert.assertEquals(messageSource.getMessage("agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
//    }
//    
//    @Test
//    public void testSaveComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
//        Agencia agencia = AgenciaTestUtil.agenciaComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
//        
//        String inputJson = super.mapToJson(agencia);
//
//        MvcResult result = mockMvc
//                .perform(MockMvcRequestBuilders.post(uri)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
//                        .content(inputJson))                
//                .andReturn();
//
//        int status = result.getResponse().getStatus();
//        String content = result.getResponse().getContentAsString();        
//
//        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
//        
//        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), status);
//        Assert.assertEquals(TestUtil.VALIDATION_EXCEPTION, errorResponse.getException());
//        Assert.assertEquals(messageSource.getMessage("agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponse.getMessages().get(0));
//    }
    
    private Agencia createAgencia() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agencia();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
}