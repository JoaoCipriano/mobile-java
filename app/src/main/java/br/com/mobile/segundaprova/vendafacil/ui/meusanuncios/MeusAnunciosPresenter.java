package br.com.mobile.segundaprova.vendafacil.ui.meusanuncios;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.mobile.segundaprova.vendafacil.helper.ConfiguracaoFirebase;
import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public class MeusAnunciosPresenter implements MeusAnunciosContract.MeusAnunciosPresenter {

    private final MeusAnunciosContract.MeusAnunciosView view;
    private final DatabaseReference anuncioUsuarioRef;
    private final List<Anuncio> listaMeusAnuncios = new ArrayList<>();

    public MeusAnunciosPresenter(MeusAnunciosContract.MeusAnunciosView view) {
        this.view = view;
        anuncioUsuarioRef = ConfiguracaoFirebase.getFirebase()
                .child("meus_anuncios")
                .child( ConfiguracaoFirebase.getIdUsuario() );
    }

    @Override
    public void recuperarAnuncios() {

        anuncioUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaMeusAnuncios.clear();
                for ( DataSnapshot ds : dataSnapshot.getChildren() ){
                    listaMeusAnuncios.add( ds.getValue(Anuncio.class) );
                }

                Collections.reverse( listaMeusAnuncios );
                view.mostrarAnuncios(listaMeusAnuncios);
                view.dismissDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
