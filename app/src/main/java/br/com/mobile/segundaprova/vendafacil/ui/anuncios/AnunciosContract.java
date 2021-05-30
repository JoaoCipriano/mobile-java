package br.com.mobile.segundaprova.vendafacil.ui.anuncios;

import java.util.List;

import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public interface AnunciosContract {

    interface AnunciosView {

        void mostrarAnuncios(List<Anuncio> anuncios);

        void dismissDiolog();
    }

    interface AnunciosPresenter {

        void recuperarAnunciosPublicos();

        void recuperarAnunciosPorEstado(String filtroEstado);

        void recuperarAnunciosPorCategoria(String filtroEstado, String filtroCategoria);

        void destruirView();
    }
}
