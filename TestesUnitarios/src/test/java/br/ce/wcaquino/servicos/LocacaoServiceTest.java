package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
     * @AfterClass  - Executa após da execução da classe
     */
    @Before
    public void setUp() {
        service = new LocacaoService();
    }

    @Test
    public void testeLocacao() throws Exception {
        // Cenário
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

        // Usando o error collector
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getValor(), is(not(6.0)));
    }

    @Test(expected = Exception.class)
    public void testLocacao_FilmeSemEstoque() throws Exception {
        // Cenário
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 0, 5.00);
        List<Filme> filmes = Arrays.asList(filme);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = service.alugarFilme(usuario, filmes);
    }

    @Test
    public void testLocacao_FilmeSemEstoque2() throws Exception {
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
    public void testLocacao_FilmeSemEstoque3() throws Exception {
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
    public void testLocacao_UsuarioVazio() throws Exception {
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
    public void testLocacao_FilmeVazio() throws Exception {
        // Cenário
        Usuario usuario = new Usuario("Mariel");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario, null);
    }
}
