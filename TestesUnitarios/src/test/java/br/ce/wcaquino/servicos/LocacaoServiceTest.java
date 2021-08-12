package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testeLocacao() throws Exception {
        // Cenário
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 1, 5.00);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = new LocacaoService().alugarFilme(usuario, filme);

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
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = new LocacaoService().alugarFilme(usuario, filme);
    }

    @Test
    public void testLocacao_FilmeSemEstoque2() throws Exception {
        // Cenário
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 0, 5.00);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        LocacaoService service = new LocacaoService();

        try {
            service.alugarFilme(usuario, filme);
            Assert.fail("Deveria ter lançado uma excessão");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme não disponível em estoque"));
        }

    }

    @Test
    public void testLocacao_FilmeSemEstoque3() throws Exception {
        // Cenário
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 0, 5.00);

        exception.expect(Exception.class);
        exception.expectMessage("Filme não disponível em estoque");

        // Execução
        service.alugarFilme(usuario, filme);
    }

    @Test
    public void testLocacao_UsuarioVazio() throws Exception {
        // Cenário
        LocacaoService service = new LocacaoService();
        Filme filme = new Filme("O Regresso", 0, 5.00);

        try {
            service.alugarFilme(null, filme);
            Assert.fail("Deveria lançar excessão");
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void testLocacao_FilmeVazio() throws Exception {
        // Cenário
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Mariel");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario, null);
    }
}
