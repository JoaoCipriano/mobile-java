package br.com.mobile.segundaprova.vendafacil.ui.detalhesproduto;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public class DetalhesProdutoPresenter implements DetalhesProdutoContract.DetalhesProdutoPresenter {

    private DetalhesProdutoContract.DetalhesProdutoView view;

    public DetalhesProdutoPresenter(DetalhesProdutoContract.DetalhesProdutoView view) {
        this.view = view;
    }


    @Override
    public void apagarAnuncio(Anuncio anuncio) {
        anuncio.remover();
        view.startActivity();
    }
}
