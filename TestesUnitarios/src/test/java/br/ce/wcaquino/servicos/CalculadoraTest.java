package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoDeveFazerDivisaoPorZeroException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setUp() {
        calc = new Calculadora();
    }

    @Test
    public void calculadora_teste_soma() {
        int resultado = calc.soma(6, 3);
        assertThat(resultado, is(9));
    }

    @Test
    public void calculadora_teste_subtracao() {
        int resultado = calc.subtrai(4, 3);
        assertThat(resultado, is(1));
    }

    @Test
    public void calculadora_teste_divisao() throws NaoDeveFazerDivisaoPorZeroException {
        int resultado = calc.dividi(6, 3);
        assertThat(resultado, is(2));
    }

    @Test(expected = NaoDeveFazerDivisaoPorZeroException.class)
    public void calculadora_deve_lancar_excessao_quando_dividi_por_zero() throws NaoDeveFazerDivisaoPorZeroException {
        int resultado = calc.dividi(6, 0);
    }

}
