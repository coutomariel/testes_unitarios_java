package br.ce.wcaquino.servicos.matchers;

import java.util.Calendar;

public class CustomMatchers {

    public static DiaSemanaMatcher caiEm(Integer diaSemana) {
        return new DiaSemanaMatcher(diaSemana);

    }

    public static DiaSemanaMatcher caiNumaSegunda() {
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static DiferencaDeDiasMarcher ehHojeComDiferencaDeDias(Integer numeroDeDias){
        return new DiferencaDeDiasMarcher(numeroDeDias);
    }

    public static DiferencaDeDiasMarcher ehDiaSeguinte(){
        return new DiferencaDeDiasMarcher(1);
    }

}
