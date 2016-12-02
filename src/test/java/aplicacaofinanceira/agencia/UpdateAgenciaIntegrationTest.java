package aplicacaofinanceira.agencia;

import aplicacaofinanceira.BaseIntegrationTest;
import aplicacaofinanceira.deserializer.AgenciaWithEnderecoAndBancoDeserializer;
import aplicacaofinanceira.deserializer.ErrorResponseDeserializer;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.AgenciaRepository;
import aplicacaofinanceira.repository.BancoRepository;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.EstadoRepository;
import aplicacaofinanceira.util.AgenciaTestUtil;
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.EnderecoTestUtil;
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
public class UpdateAgenciaIntegrationTest extends BaseIntegrationTest {

    private String uri = AgenciaTestUtil.AGENCIAS_URI + TestUtil.ID_COMPLEMENT_URI;
    
    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private BancoRepository bancoRepository;
    
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
        Agencia agenciaBairro = createValidAgenciaBairro();
 
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        String inputJson = super.mapToJson(agenciaBairro);

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
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        String inputJson = super.mapToJson(agenciaBairro);

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
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaCampus = createValidAgenciaCampus();
        
        String inputJson = super.mapToJson(agenciaCampus);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put(uri, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization())
                        .content(inputJson))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();        

        AgenciaWithEnderecoAndBancoDeserializer agenciaWithEnderecoAndBancoDeserializer = super.mapFromJsonObject(content, AgenciaWithEnderecoAndBancoDeserializer.class);
        
        Assert.assertEquals(HttpStatus.OK.value(), status);
        Assert.assertNotNull(agenciaWithEnderecoAndBancoDeserializer.getAgenciaId());
        Assert.assertNotNull(agenciaWithEnderecoAndBancoDeserializer.getEnderecoId());
        Assert.assertEquals(agenciaBairro.getNome(), agenciaWithEnderecoAndBancoDeserializer.getAgenciaNome());
        Assert.assertEquals(agenciaBairro.getNumero(), agenciaWithEnderecoAndBancoDeserializer.getAgenciaNumero());        
        Assert.assertEquals(agenciaBairro.getEndereco().getLogradouro(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoLogradouro());
        Assert.assertEquals(agenciaBairro.getEndereco().getNumero(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoNumero());
        Assert.assertEquals(agenciaBairro.getEndereco().getComplemento(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoComplemento());
        Assert.assertEquals(agenciaBairro.getEndereco().getBairro(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoBairro());
        Assert.assertEquals(agenciaBairro.getEndereco().getCep(), agenciaWithEnderecoAndBancoDeserializer.getEnderecoCep());
        Assert.assertEquals(agenciaBairro.getEndereco().getCidade().getId(), agenciaWithEnderecoAndBancoDeserializer.getCidadeId());
        Assert.assertEquals(agenciaBairro.getEndereco().getCidade().getNome(), agenciaWithEnderecoAndBancoDeserializer.getCidadeNome());
        Assert.assertEquals(agenciaBairro.getEndereco().getCidade().getEstado().getId(), agenciaWithEnderecoAndBancoDeserializer.getEstadoId());
        Assert.assertEquals(agenciaBairro.getEndereco().getCidade().getEstado().getNome(), agenciaWithEnderecoAndBancoDeserializer.getEstadoNome());
        Assert.assertEquals(agenciaBairro.getBanco().getId(), agenciaWithEnderecoAndBancoDeserializer.getBancoId());
        Assert.assertEquals(agenciaBairro.getBanco().getNome(), agenciaWithEnderecoAndBancoDeserializer.getBancoNome());
    }  
    
    @Test
    public void testUpdateComBancoInvalido() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWithInvalidBanco = createAgenciaWithInvalidBanco();
        
        String inputJson = super.mapToJson(agenciaWithInvalidBanco);

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
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNaoEncontrado", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testUpdateComCidadeInvalida() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWithInvalidCidade = createAgenciaWithInvalidCidade();
        
        String inputJson = super.mapToJson(agenciaWithInvalidCidade);

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
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponseDeserializer.getException());
        Assert.assertEquals(messageSource.getMessage("cidadeNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    }
    
    @Test
    public void testUpdateSemCamposObrigatorios() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaSemCamposObrigatorios = AgenciaTestUtil.agenciaSemCamposObrigatorios();
        
        String inputJson = super.mapToJson(agenciaSemCamposObrigatorios);

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
        Assert.assertEquals(messageSource.getMessage("enderecoCepNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(0));
        Assert.assertEquals(messageSource.getMessage("enderecoCepInvalido", null, null), errorResponseDeserializer.getMessages().get(1));
        Assert.assertEquals(messageSource.getMessage("enderecoBairroNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(2));
        Assert.assertEquals(messageSource.getMessage("enderecoLogradouroNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(3));
        Assert.assertEquals(messageSource.getMessage("agenciaNomeNaoPodeSerNulo", null, null), errorResponseDeserializer.getMessages().get(4));
        Assert.assertEquals(messageSource.getMessage("agenciaNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(5));
        Assert.assertEquals(messageSource.getMessage("enderecoNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(6));
    }
    
    @Test
    public void testUpdateComNumeroMenorDoQueUm() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWithNumberLessThanOne = createAgenciaWithNumberLessThanOne();
        
        String inputJson = super.mapToJson(agenciaWithNumberLessThanOne);

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
        Assert.assertEquals(messageSource.getMessage("agenciaNumeroDeveSerMaiorDoQueZero", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNumeroDuplicado() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        Agencia agenciaCampus = createValidAgenciaCampus();
        
        agenciaRepository.save(agenciaBairro);
        agenciaRepository.save(agenciaCampus);
        
        Long id = agenciaCampus.getId();
        
        String inputJson = super.mapToJson(agenciaBairro);

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
        Assert.assertEquals(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null), errorResponseDeserializer.getMessage());
    }    
    
    @Test
    public void testUpdateComNomeComMenosDeDoisCaracteres() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWhichNomeHasLessThanTwoCharacters = createAgenciaWhichNomeHasLessThanTwoCharacters();
        
        String inputJson = super.mapToJson(agenciaWhichNomeHasLessThanTwoCharacters);

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
        Assert.assertEquals(messageSource.getMessage("agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComNomeComMaisDeDuzentosECinquentaECincoCaracteres() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWhichNomeHasMoreThan255Characters = createAgenciaWhichNomeHasMoreThan255Characters();
        
        String inputJson = super.mapToJson(agenciaWhichNomeHasMoreThan255Characters);

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
        Assert.assertEquals(messageSource.getMessage("agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComCepComMenosDeNoveCaracteres() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWhichCepHasLessThanNineCharacters = createAgenciaWhichCepHasLessThanNineCharacters();
        
        String inputJson = super.mapToJson(agenciaWhichCepHasLessThanNineCharacters);

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
        Assert.assertEquals(messageSource.getMessage("enderecoCepInvalido", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComCepComMaisDeNoveCaracteres() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWhichCepHasMoreThanNineCharacters = createAgenciaWhichCepHasMoreThanNineCharacters();
        
        String inputJson = super.mapToJson(agenciaWhichCepHasMoreThanNineCharacters);

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
        Assert.assertEquals(messageSource.getMessage("enderecoCepInvalido", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComCepInvalido() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        Long id = agenciaBairro.getId();
        
        Agencia agenciaWithInvalidCep = createAgenciaWithInvalidCep();
        
        String inputJson = super.mapToJson(agenciaWithInvalidCep);

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
        Assert.assertEquals(messageSource.getMessage("enderecoCepInvalido", null, null), errorResponseDeserializer.getMessages().get(0));
    }
    
    @Test
    public void testUpdateComAgenciaInexistente() throws Exception {
        Agencia agenciaBairro = createValidAgenciaBairro();
        
        agenciaRepository.save(agenciaBairro);
        
        String inputJson = super.mapToJson(agenciaBairro);

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
        Assert.assertEquals(messageSource.getMessage("agenciaNaoEncontrada", null, null), errorResponseDeserializer.getMessage());
    }
    
    private Agencia createValidAgenciaBairro() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaBairro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createValidAgenciaCampus() {
        Estado estado = EstadoTestUtil.rioDeJaneiro();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.rioDeJaneiro();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.caixaEconomicaFederal();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaCampus();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createAgenciaWhichCepHasLessThanNineCharacters() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaComCepComMenosDeNoveCaracteres();
        agencia.setBanco(banco);

        return agencia;
    }

    private Agencia createAgenciaWhichCepHasMoreThanNineCharacters() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaComCepComMaisDeNoveCaracteres();
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createAgenciaWhichNomeHasLessThanTwoCharacters() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaComNomeComMenosDeDoisCaracteres();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }

    private Agencia createAgenciaWhichNomeHasMoreThan255Characters() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaComNomeComMaisDeDuzentosECinquentaECincoCaracteres();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createAgenciaWithInvalidBanco() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);

        Banco banco = new Banco();
        banco.setId(new Long(0));
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createAgenciaWithInvalidCep() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaComCepInvalido();
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createAgenciaWithInvalidCidade() {
        Cidade cidade = new Cidade();
        cidade.setId(new Long(0));
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaCentro();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }
    
    private Agencia createAgenciaWithNumberLessThanOne() {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        Agencia agencia = AgenciaTestUtil.agenciaComNumeroMenorDoQueUm();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);

        return agencia;
    }    
}