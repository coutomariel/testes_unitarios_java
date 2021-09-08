package br.ce.wcaquino.servicos.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    public FilmeBuilder() {
    }

    public static FilmeBuilder umFilme() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome("Filme 1");
        builder.filme.setEstoque(1);
        builder.filme.setPrecoLocacao(4d);
        return builder;
    }

    public static FilmeBuilder umFilmeSemEstoque() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setNome("Filme 1");
        builder.filme.setEstoque(0);
        builder.filme.setPrecoLocacao(4d);
        return builder;
    }

    public Filme agora() {
        return filme;
    }
}
