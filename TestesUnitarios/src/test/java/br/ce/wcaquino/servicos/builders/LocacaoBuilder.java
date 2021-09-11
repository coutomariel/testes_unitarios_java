package br.ce.wcaquino.servicos.builders;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Date;
import java.util.List;

public class LocacaoBuilder {

    private Locacao locacao;

    public LocacaoBuilder() {
    }

    public static LocacaoBuilder umaLocacao(){
        LocacaoBuilder builder = new LocacaoBuilder();
        builder.locacao = new Locacao();
        builder.locacao.setDataLocacao(new Date());
        builder.locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(2));
        return builder;
    }

    public LocacaoBuilder comOsFilmes(List<Filme> filmes){
        this.locacao.setFilme(filmes);
        return this;
    }

    public LocacaoBuilder comUsuario(Usuario usuario){
        this.locacao.setUsuario(usuario);
        return this;
    }

    public LocacaoBuilder comRetornoPara(Date dataRetorno){
        this.locacao.setDataLocacao(DataUtils.adicionarDias(dataRetorno, -1));
        this.locacao.setDataRetorno(dataRetorno);
        return this;
    }

    public Locacao agora() {
        return locacao;
    }
}
