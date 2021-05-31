package br.com.mobile.segundaprova.vendafacil.ui.editaranuncio;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public interface EditarAnuncioContract {

    interface EditarAnuncioView {

        void startActivity();
    }

    interface EditarAnuncioPresenter {

        void atualizarAnuncio(Anuncio anuncioSelecionado, Anuncio anuncio);

    }
}
