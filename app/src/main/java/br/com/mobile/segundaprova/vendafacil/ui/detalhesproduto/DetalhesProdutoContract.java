package br.com.mobile.segundaprova.vendafacil.ui.detalhesproduto;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public interface DetalhesProdutoContract {

    interface DetalhesProdutoView {

        void startActivity();

    }

    interface  DetalhesProdutoPresenter {

        void apagarAnuncio(Anuncio anuncio);

    }
}
