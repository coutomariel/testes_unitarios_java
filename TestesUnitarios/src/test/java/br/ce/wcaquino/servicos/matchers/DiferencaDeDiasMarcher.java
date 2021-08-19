package br.ce.wcaquino.servicos.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DiferencaDeDiasMarcher extends TypeSafeMatcher<Date> {

    private Integer numeroDeDias;

    public DiferencaDeDiasMarcher(Integer numeroDeDias) {
        this.numeroDeDias = numeroDeDias;
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(numeroDeDias));
    }

    @Override
    public void describeTo(Description description) {

    }
}
