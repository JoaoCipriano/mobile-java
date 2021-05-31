package br.com.mobile.segundaprova.vendafacil.ui.editaranuncio;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public class EditarAnuncioPresenter implements EditarAnuncioContract.EditarAnuncioPresenter {

    private EditarAnuncioContract.EditarAnuncioView view;

    public EditarAnuncioPresenter(EditarAnuncioContract.EditarAnuncioView view) {
        this.view = view;
    }

    @Override
    public void atualizarAnuncio(Anuncio anuncioSelecionado, Anuncio anuncio) {
        anuncioSelecionado.remover();
        anuncio.salvar();
        view.startActivity();
    }
}
