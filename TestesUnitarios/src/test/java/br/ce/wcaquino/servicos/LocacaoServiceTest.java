package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.UsuarioNegativadoException;
import br.ce.wcaquino.servicos.builders.FilmeBuilder;
import br.ce.wcaquino.servicos.builders.LocacaoBuilder;
import br.ce.wcaquino.servicos.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.servicos.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.servicos.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.servicos.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.servicos.matchers.CustomMatchers.*;
import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LocacaoService service;
    private SPCService spcService;
    private LocacaoDao dao;
    private EmailService emailService;

    /**
     * @Before - Executa antes de cada método de teste
     * @After - Executa após de cada método de teste
     * @BeforeClass - Executa antes da execução da classe
     * @AfterClass - Executa após da execução da classe
     */
    @Before
    public void setUp() {
        service = new LocacaoService();

        dao = mock(LocacaoDao.class);
        service.setDao(dao);

        spcService = mock(SPCService.class);
        service.setSpc(spcService);

        emailService = mock(EmailService.class);
        service.setEmail(emailService);

    }

    @Test
    public void deveAlugarFilme() throws Exception {
        // Cenário
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        Usuario usuario = umUsuario().agora();
        Filme filme = FilmeBuilder.umFilme().agora();
        List<Filme> filmes = Arrays.asList(filme);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // Validação
        assertTrue(locacao.getValor() == 4.0);
//        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

//        Desafio
        assertThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
        assertThat(locacao.getDataRetorno(), ehDiaSeguinte());


        // Usando o error collector
        error.checkThat(locacao.getValor(), is(equalTo(4.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
    }

    @Test(expected = Exception.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        // Cenário
        Usuario usuario = umUsuario().agora();
        Filme filme = umFilmeSemEstoque().agora();

        List<Filme> filmes = Arrays.asList(filme);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque2() throws Exception {
        // Cenário
        Usuario usuario = umUsuario().agora();
        Filme filme = umFilmeSemEstoque().agora();
        Date dataEsperada = adicionarDias(new Date(), 2);

        List<Filme> filmes = Arrays.asList(filme);

        try {
            service.alugarFilme(usuario, filmes);
            Assert.fail("Deveria ter lançado uma excessão");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme não disponível em estoque"));
        }

    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque3() throws Exception {
        // Cenário
        Usuario usuario = umUsuario().agora();
        Filme filme = umFilmeSemEstoque().agora();
        List<Filme> filmes = Arrays.asList(filme);

        exception.expect(Exception.class);
        exception.expectMessage("Filme não disponível em estoque");

        // Execução
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws Exception {
        // Cenário
        Filme filme = umFilme().agora();
        List<Filme> filmes = Arrays.asList(filme);
        try {
            service.alugarFilme(null, filmes);
            Assert.fail("Deveria lançar excessão");
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void naoDeveRealizarLocacaoSemFilme() throws Exception {
        // Cenário
        Usuario usuario = umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario, null);
    }

    @Test
//    @Ignore
    public void naoDeveDevolverFilmeNoDomingo() throws Exception {
        assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        Usuario usuario = umUsuario().agora();
        Filme oRegresso = umFilme().agora();

        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(oRegresso));

        assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
        assertThat(locacao.getDataRetorno(), caiNumaSegunda());

    }

    @Test
    public void naoDeveAlugarFilmeParaClienteNegativado() throws Exception {
        // cenário
        Usuario usuario = umUsuario().agora();
        Filme oRegresso = umFilme().agora();

        when(spcService.possuiNegativacao(usuario)).thenReturn(true);

//        exception.expect(UsuarioNegativadoException.class);
//        exception.expectMessage("Usuario negativado");

        try {
            service.alugarFilme(usuario, Arrays.asList(oRegresso));
            Assert.fail("Deveria falhar");
        } catch (UsuarioNegativadoException e) {
            Assert.assertThat(e.getMessage(), is("Usuario negativado."));
        }
        verify(spcService).possuiNegativacao(usuario);

    }

    @Test
    public void deveNotificarUsuariosComDevolucoesPendentes() {
        // cenário
        Usuario usuario = umUsuario().agora();
        Filme oRegresso = umFilme().agora();

        List<Locacao> locacoes = Arrays.asList(
                LocacaoBuilder.umaLocacao()
                        .comOsFilmes(Arrays.asList(oRegresso))
                        .comUsuario(usuario)
                        .comRetornoPara(DataUtils.obterData(9, 9, 21))
                        .agora());

        when(dao.buscaLocacoes()).thenReturn(locacoes);

        service.notificaUsuariosComDevolucoesPendentes();

        verify(emailService).notificaAtrasoParaUsuario(usuario);

    }

}
