package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.matchers.CustomMatchers;
import br.ce.wcaquino.servicos.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.*;
import java.util.logging.Logger;

import static br.ce.wcaquino.servicos.matchers.CustomMatchers.*;
import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.*;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private LocacaoService service;

    /**
     * @Before - Executa antes de cada método de teste
     * @After - Executa após de cada método de teste
     * @BeforeClass - Executa antes da execução da classe
     * @AfterClass - Executa após da execução da classe
     */
    @Before
    public void setUp() {
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        // Cenário
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        Usuario usuario = new Usuario("Mariel");

        Filme filme = new Filme("O Regresso", 1, 5.00);
        List<Filme> filmes = Arrays.asList(filme);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = service.alugarFilme(usuario, filmes);

        // Validação
        assertTrue(locacao.getValor() == 5.0);
//        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

//        Desafio
        assertThat(locacao.getDataRetorno(), ehHojeComDiferencaDeDias(1));
        assertThat(locacao.getDataRetorno(), ehDiaSeguinte());


        // Usando o error collector
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
    }

    @Test(expected = Exception.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        // Cenário
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 0, 5.00);
        List<Filme> filmes = Arrays.asList(filme);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque2() throws Exception {
        // Cenário
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 0, 5.00);
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
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 0, 5.00);
        List<Filme> filmes = Arrays.asList(filme);

        exception.expect(Exception.class);
        exception.expectMessage("Filme não disponível em estoque");

        // Execução
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws Exception {
        // Cenário
        Filme filme = new Filme("O Regresso", 0, 5.00);
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
        Usuario usuario = new Usuario("Mariel");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario, null);
    }

    @Test
//    @Ignore
    public void naoDeveDevolverFilmeNoDomingo() throws Exception {
        assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        Usuario usuario = new Usuario("Usuario 1");
        Filme oRegresso = new Filme("O Regresso", 1, 4d);

        Locacao locacao = service.alugarFilme(usuario, Arrays.asList(oRegresso));

        assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
        assertThat(locacao.getDataRetorno(), caiNumaSegunda());

    }

}
