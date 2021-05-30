package br.com.mobile.segundaprova.vendafacil.ui.meusanuncios;

import java.util.List;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public interface MeusAnunciosContract {

    interface MeusAnunciosView {

        void mostrarAnuncios(List<Anuncio> anuncios);

        void dismissDialog();

        void notifyDataSetChangedAdapter();
    }

    interface MeusAnunciosPresenter {

        void recuperarAnuncios();
    }
}
