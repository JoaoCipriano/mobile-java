package br.com.mobile.segundaprova.vendafacil.ui.anuncios;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.mobile.segundaprova.vendafacil.helper.ConfiguracaoFirebase;
import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public class AnunciosPresenter implements AnunciosContract.AnunciosPresenter {

    private AnunciosContract.AnunciosView view;
    private DatabaseReference anunciosPublicosRef;
    private final List<Anuncio> listaAnuncios = new ArrayList<>();

    public  AnunciosPresenter(AnunciosContract.AnunciosView view) {
        this.view = view;
        this.anunciosPublicosRef = ConfiguracaoFirebase.getFirebase()
                .child("anuncios");
    }

    @Override
    public void recuperarAnunciosPublicos() {
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot estados: dataSnapshot.getChildren()){
                    for (DataSnapshot categorias: estados.getChildren() ){
                        for(DataSnapshot anuncios: categorias.getChildren() ){

                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            listaAnuncios.add( anuncio );
                        }
                    }
                }
                Collections.reverse( listaAnuncios );
                view.mostrarAnuncios(listaAnuncios);
                view.dismissDiolog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void recuperarAnunciosPorEstado(String filtroEstado) {
        //Configura nó por estado
        anunciosPublicosRef = ConfiguracaoFirebase.getFirebase()
                .child("anuncios")
                .child(filtroEstado);

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for (DataSnapshot categorias: dataSnapshot.getChildren() ) {
                    for(DataSnapshot anuncios: categorias.getChildren() ) {

                        Anuncio anuncio = anuncios.getValue(Anuncio.class);
                        listaAnuncios.add( anuncio );
                    }
                }

                Collections.reverse( listaAnuncios );
                view.mostrarAnuncios(listaAnuncios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void recuperarAnunciosPorCategoria(String filtroEstado, String filtroCategoria) {
        //Configura nó por categoria
        anunciosPublicosRef = ConfiguracaoFirebase.getFirebase()
                .child("anuncios")
                .child(filtroEstado)
                .child( filtroCategoria );

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for(DataSnapshot anuncios: dataSnapshot.getChildren() ){

                    Anuncio anuncio = anuncios.getValue(Anuncio.class);
                    listaAnuncios.add( anuncio );
                }

                Collections.reverse( listaAnuncios );
                view.mostrarAnuncios(listaAnuncios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void destruirView() {
        this.view = null;
    }
}
