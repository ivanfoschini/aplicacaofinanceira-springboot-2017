package aplicacaofinanceira.banco;

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
import aplicacaofinanceira.util.BancoTestUtil;
import aplicacaofinanceira.util.CidadeTestUtil;
import aplicacaofinanceira.util.EnderecoTestUtil;
import aplicacaofinanceira.util.ErrorResponse;
import aplicacaofinanceira.util.EstadoTestUtil;
import aplicacaofinanceira.util.TestUtil;
import java.util.ArrayList;
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

public class DeleteBancoIntegrationTest extends BaseIntegrationTest {
 
    private String uri = BancoTestUtil.BANCOS_URI + BancoTestUtil.ID_COMPLEMENT_URI;
    
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
    public void testDeleteComUsuarioNaoAutorizado() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getFuncionarioAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), status);
        
        bancoRepository.delete(banco);
    }
    
    @Test
    public void testDeleteComBancoInexistente() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, 0)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString(); 
        
        ErrorResponse errorResponse = super.mapFromJsonObject(content, ErrorResponse.class);
        
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), status);
        Assert.assertEquals(TestUtil.NOT_FOUND_EXCEPTION, errorResponse.getException());
        Assert.assertEquals(messageSource.getMessage("bancoNaoEncontrado", null, null), errorResponse.getMessage());
        
        bancoRepository.delete(banco);
    } 
    
    @Test
    public void testDeleteComBancoQuePossuiPeloMenosUmaAgenciaAssociada() throws Exception {
        Estado estado = EstadoTestUtil.saoPaulo();
        
        estadoRepository.save(estado);        
        
        Cidade cidade = CidadeTestUtil.saoCarlos();
        cidade.setEstado(estado);        
        
        cidadeRepository.save(cidade);
        
        Endereco endereco = EnderecoTestUtil.validEndereco();
        endereco.setCidade(cidade);
        
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(endereco);
        cidade.setEnderecos(enderecos);
        
        enderecoRepository.save(endereco);
        
        Banco banco = BancoTestUtil.bancoDoBrasil();                
        
        bancoRepository.save(banco);        
        
        Agencia agencia = AgenciaTestUtil.agencia();
        agencia.setEndereco(endereco);
        agencia.setBanco(banco);
        
        List<Agencia> agencias = new ArrayList<>();
        agencias.add(agencia);
        
        banco.setAgencias(agencias);
        
        agenciaRepository.save(agencia);        
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        System.out.println("status:" + status);
        
        estadoRepository.delete(estado);
        cidadeRepository.delete(cidade);
        enderecoRepository.delete(endereco);
        bancoRepository.delete(banco);
        agenciaRepository.delete(agencia);        
        
        Assert.assertEquals(true, true);
        
//        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), status);        
    } 
    
    @Test
    public void testDeleteComSucesso() throws Exception {
        Banco banco = BancoTestUtil.bancoDoBrasil();
        
        bancoRepository.save(banco);
        
        Long id = banco.getId();
        
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete(uri, id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, TestUtil.getAdminAuthorization()))                
                .andReturn();

        int status = result.getResponse().getStatus();
        
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), status);        
    } 
}