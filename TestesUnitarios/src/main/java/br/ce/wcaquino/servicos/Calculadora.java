package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoDeveFazerDivisaoPorZeroException;

public class Calculadora {
    public int soma(int a, int b) {
        return a + b;
    }

    public int subtrai(int a, int b) {
        return a - b;
    }

    public int dividi(int a, int b) throws NaoDeveFazerDivisaoPorZeroException {
        if(b == 0){
            throw new NaoDeveFazerDivisaoPorZeroException("Nao eh possivel fazer divisao por zero");
        }
        return a / b;
    }
}
