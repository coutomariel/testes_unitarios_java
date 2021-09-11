package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.UsuarioNegativadoException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import static br.ce.wcaquino.utils.DataUtils.verificarDiaSemana;

public class LocacaoService {

    private LocacaoDao dao;
    private SPCService spc;
    private EmailService email;

    public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception {

        if (usuario == null) {
            throw new LocadoraException("Usuario vazio");
        }
        if (filmes == null) {
            throw new LocadoraException("Filme vazio");
        }

        Double totalVlLocacao = 0.0;

        for (int i = 0; i < filmes.size(); i++) {
            if (filmes.get(i).getEstoque() == 0) {
                throw new FilmeSemEstoqueException("Filme não disponível em estoque");
            }
            switch (i) {
                case 2 :
                    totalVlLocacao += filmes.get(i).getPrecoLocacao() * 0.75;
                    break;
                case 3:
                    totalVlLocacao += filmes.get(i).getPrecoLocacao() * 0.5;
                    break;
                case 4:
                    totalVlLocacao += filmes.get(i).getPrecoLocacao() * 0.25;
                    break;
                case 5:
                    totalVlLocacao += filmes.get(i).getPrecoLocacao() * 0;
                    break;
                default:
                    totalVlLocacao += filmes.get(i).getPrecoLocacao();
                    break;
            }

        }

        if(spc.possuiNegativacao(usuario)){
            throw new UsuarioNegativadoException("Usuario negativado.");
        }

        Locacao locacao = new Locacao();
        locacao.setFilme(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());

        Double valorLocacao = 0.0;
        locacao.setValor(totalVlLocacao);

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, 1);
        if(verificarDiaSemana(dataEntrega, Calendar.SUNDAY)){
            dataEntrega = adicionarDias(dataEntrega, 1);
        }

        locacao.setDataRetorno(dataEntrega);

        //Salvando a locacao...
        //TODO adicionar método para salvar
        dao.salvar(locacao);

        return locacao;
    }

    public void notificaUsuariosComDevolucoesPendentes(){
        List<Locacao> locacoes = dao.buscaLocacoes();
        for (Locacao locacao : locacoes) {
            if(locacao.getDataRetorno().before(new Date())){
                email.notificaAtrasoParaUsuario(locacao.getUsuario());
            }
        }
    }

    public void setDao(LocacaoDao dao) {
        this.dao = dao;
    }

    public void setEmail(EmailService email){
        this.email = email;
    }

    public void setSpc(SPCService spc) {
        this.spc = spc;
    }
}