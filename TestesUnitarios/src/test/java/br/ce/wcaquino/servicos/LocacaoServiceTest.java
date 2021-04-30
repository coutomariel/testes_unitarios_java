package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.*;

public class LocacaoServiceTest {

    @Test
    public void firstTest() {
        // Cenário
        Usuario usuario = new Usuario("Mariel");
        Filme filme = new Filme("O Regresso", 1, 5.00);
        Date dataEsperada = adicionarDias(new Date(), 2);

        // Execução
        Locacao locacao = new LocacaoService().alugarFilme(usuario, filme);

        // Validação
        Assert.assertTrue(locacao.getValor() == 5.0);
        Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
    }
}
