package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class CalculaValorLocacaoTest {

    @Parameter
    public List<Filme> filmes;
    @Parameter(value = 1)
    public Double valorLocacao;
    @Parameter(value = 2)
    public String descricao;

    public LocacaoService service;

    @Before
    public void setUp() {
        service = new LocacaoService();
    }

    public static Filme filme1 = new Filme("Filme1", 1, 4d);
    public static Filme filme2 = new Filme("Filme2", 1, 4d);
    public static Filme filme3 = new Filme("Filme3", 1, 4d);
    public static Filme filme4 = new Filme("Filme4", 1, 4d);
    public static Filme filme5 = new Filme("Filme5", 1, 4d);
    public static Filme filme6 = new Filme("Filme6", 1, 4d);

    @Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2, filme3), 11d, "25% desc terceiro filme"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13d, "50% desc quarto filme"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14d, "75% desc quinto filme"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14d, "100% desc sexto filme"},
        });
    }


    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
        Usuario usuario = new Usuario("Usuario 1");

        Locacao locacao = service.alugarFilme(usuario, filmes);

        assertThat(locacao.getValor(), is(valorLocacao));

    }
}
