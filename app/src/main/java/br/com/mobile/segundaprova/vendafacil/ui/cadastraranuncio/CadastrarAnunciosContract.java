package br.com.mobile.segundaprova.vendafacil.ui.cadastraranuncio;

import java.util.List;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public interface CadastrarAnunciosContract {

    interface CadastrarAnunciosView {

        void dismissDiolog();

        void finishActivity();

        void startActivity();

        void exibirMensagemErro(String msg);

    }

    interface CadastrarAnunciosPresenter {

        void salvarAnuncio(Anuncio anuncio, List<String> listaFotosRecuperadas);

    }
}
