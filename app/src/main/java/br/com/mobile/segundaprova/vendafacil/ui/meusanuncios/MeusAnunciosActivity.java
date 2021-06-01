package br.com.mobile.segundaprova.vendafacil.ui.meusanuncios;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.com.mobile.segundaprova.vendafacil.R;
import br.com.mobile.segundaprova.vendafacil.adapter.AdapterAnuncios;
import br.com.mobile.segundaprova.vendafacil.helper.RecyclerItemClickListener;
import br.com.mobile.segundaprova.vendafacil.model.Anuncio;
import br.com.mobile.segundaprova.vendafacil.ui.cadastraranuncio.CadastrarAnuncioActivity;
import br.com.mobile.segundaprova.vendafacil.ui.detalhesproduto.DetalhesProdutoActivity;
import dmax.dialog.SpotsDialog;

public class MeusAnunciosActivity extends AppCompatActivity implements MeusAnunciosContract.MeusAnunciosView {

    private RecyclerView recyclerAnuncios;
    private List<Anuncio> meusAnuncios = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private AlertDialog dialog;

    private MeusAnunciosContract.MeusAnunciosPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        inicializarComponentes();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new MeusAnunciosPresenter(this);

        recuperarAnuncios();

        //Adiciona evento de clique no recyclerview
        recyclerAnuncios.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerAnuncios,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Anuncio anuncioSelecionado = meusAnuncios.get( position );
                                anuncioSelecionado.setOwner(true);
                                Intent i = new Intent(MeusAnunciosActivity.this, DetalhesProdutoActivity.class);
                                i.putExtra(getString(R.string.anuncio_selecionado), anuncioSelecionado );
                                startActivity( i );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );
    }

    private void recuperarAnuncios(){

        dialog = new SpotsDialog.Builder()
                .setContext( this )
                .setMessage(getString(R.string.recuperando_anuncios))
                .setCancelable( false )
                .build();
        dialog.show();

        meusAnuncios.clear();
        presenter.recuperarAnuncios();
    }

    public void inicializarComponentes(){
        recyclerAnuncios = findViewById(R.id.recyclerAnuncios);

        configurarRecycleView();
        configurarToolbar();
        configurarFloatingActionButton();
    }

    private void configurarRecycleView() {
        recyclerAnuncios.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(meusAnuncios, this);
        recyclerAnuncios.setAdapter( adapterAnuncios );
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configurarFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarAnuncioActivity.class));
            }
        });
    }

    @Override
    public void mostrarAnuncios(List<Anuncio> anuncios) {
        meusAnuncios.addAll(anuncios);
        adapterAnuncios.notifyDataSetChanged();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void notifyDataSetChangedAdapter() {
        adapterAnuncios.notifyDataSetChanged();
    }
}