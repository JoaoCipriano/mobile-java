package br.com.mobile.segundaprova.vendafacil.ui.detalhesproduto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import br.com.mobile.segundaprova.vendafacil.R;
import br.com.mobile.segundaprova.vendafacil.model.Anuncio;
import br.com.mobile.segundaprova.vendafacil.ui.editaranuncio.EditarAnuncioActivity;
import br.com.mobile.segundaprova.vendafacil.ui.meusanuncios.MeusAnunciosActivity;

public class DetalhesProdutoActivity extends AppCompatActivity  implements DetalhesProdutoContract.DetalhesProdutoView {

    private CarouselView carouselView;
    private TextView titulo;
    private TextView descricao;
    private TextView estado;
    private TextView preco;
    private Anuncio anuncioSelecionado;
    private Button editarAnuncio;
    private Button apagarAnuncio;

    private DetalhesProdutoContract.DetalhesProdutoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        //Configurar toolbar
        getSupportActionBar().setTitle(R.string.detalhes_do_produto);

        inicializarComponentes();

        //Recupera anúncio para exibicao
        anuncioSelecionado = (Anuncio) getIntent().getSerializableExtra(getString(R.string.anuncio_selecionado));

        presenter = new DetalhesProdutoPresenter(this);

        if( anuncioSelecionado != null ) {
            titulo.setText( anuncioSelecionado.getTitulo() );
            descricao.setText( anuncioSelecionado.getDescricao() );
            estado.setText( anuncioSelecionado.getEstado() );
            preco.setText( anuncioSelecionado.getValor());

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = anuncioSelecionado.getFotos().get( position );
                    Picasso.get().load(urlString).into(imageView);
                }
            };

            carouselView.setPageCount( anuncioSelecionado.getFotos().size() );
            carouselView.setImageListener( imageListener );

            if (anuncioSelecionado.isOwner())
                habilitarButtonsEditarApagarAnuncio();
        }
    }

    public void visualizarTelefone(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", anuncioSelecionado.getTelefone(), null ));
        startActivity( i );
    }

    public void editarAnuncio(View view) {
        Intent i = new Intent(getApplicationContext(), EditarAnuncioActivity.class);
        i.putExtra(getString(R.string.anuncio_selecionado), anuncioSelecionado );
        startActivity(i);
    }

    public void apagarAnuncio(View view) {
        presenter.apagarAnuncio(anuncioSelecionado);
    }

    private void habilitarButtonsEditarApagarAnuncio() {
        editarAnuncio.setVisibility(View.VISIBLE);
        apagarAnuncio.setVisibility(View.VISIBLE);
    }

    private void inicializarComponentes(){
        carouselView = findViewById(R.id.carouselView);
        titulo = findViewById(R.id.textTituloDetalhe);
        descricao = findViewById(R.id.textDescricaoDetalhe);
        estado = findViewById(R.id.textEstadoDetalhe);
        preco = findViewById(R.id.textPrecoDetalhe);
        editarAnuncio = findViewById(R.id.buttonEditarAnuncio);
        apagarAnuncio = findViewById(R.id.buttonApagarAnuncio);
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(this, MeusAnunciosActivity.class));
    }
}
