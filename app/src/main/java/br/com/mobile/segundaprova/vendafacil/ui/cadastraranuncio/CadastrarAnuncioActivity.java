package br.com.mobile.segundaprova.vendafacil.ui.cadastraranuncio;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.santalu.maskedittext.MaskEditText;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.mobile.segundaprova.vendafacil.R;
import br.com.mobile.segundaprova.vendafacil.helper.AnuncioUtils;
import br.com.mobile.segundaprova.vendafacil.helper.Permissoes;
import br.com.mobile.segundaprova.vendafacil.model.Anuncio;
import br.com.mobile.segundaprova.vendafacil.ui.meusanuncios.MeusAnunciosActivity;
import dmax.dialog.SpotsDialog;

public class CadastrarAnuncioActivity extends AppCompatActivity
            implements View.OnClickListener, CadastrarAnuncioContract.CadastrarAnunciosView {

    private EditText campoTitulo, campoDescricao;
    private ImageView imagem1, imagem2, imagem3;
    private Spinner campoEstado, campoCategoria;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;
    private Anuncio anuncio;
    private AlertDialog dialog;

    private final String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private final List<String> listaFotosRecuperadas = new ArrayList<>();
    private byte[] dadosFoto = {};

    private static final int SELECAO_CAMERA = 10;
    private static final int SELECAO_GALERIA_1 = 20;
    private static final int SELECAO_GALERIA_2 = 30;

    private CadastrarAnuncioContract.CadastrarAnunciosPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();
        AnuncioUtils.carregarDadosSpinner(this, campoEstado, campoCategoria);
        presenter = new CadastrarAnuncioPresenter(this);
    }

    public void salvarAnuncio(){

        dialog = new SpotsDialog.Builder()
                .setContext( this )
                .setMessage(R.string.salvando_anuncio)
                .setCancelable( false )
                .build();
        dialog.show();

        presenter.salvarAnuncio(anuncio, listaFotosRecuperadas, dadosFoto);
    }


    private Anuncio configurarAnuncio(){

        String estado = campoEstado.getSelectedItem().toString();
        String categoria = campoCategoria.getSelectedItem().toString();
        String titulo = campoTitulo.getText().toString();
        String valor = campoValor.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String descricao = campoDescricao.getText().toString();

        Anuncio anuncio = new Anuncio();
        anuncio.setEstado( estado );
        anuncio.setCategoria(categoria);
        anuncio.setTitulo(titulo);
        anuncio.setValor(valor);
        anuncio.setTelefone( telefone );
        anuncio.setDescricao(descricao);

        return anuncio;

    }

    public void validarDadosAnuncio(View view){

        anuncio = configurarAnuncio();
        String valor = String.valueOf(campoValor.getRawValue());

        if( isHasAnImageOrPhoto() ){
            if( !anuncio.getEstado().isEmpty() ){
                if( !anuncio.getCategoria().isEmpty() ){
                    if( !anuncio.getTitulo().isEmpty() ){
                        if( !valor.isEmpty() && !valor.equals("0") ){
                            if( !anuncio.getTelefone().isEmpty()  ){
                                if( !anuncio.getDescricao().isEmpty() ){

                                    salvarAnuncio();

                                }else {
                                    exibirMensagemErro(getString(R.string.preencha_o_campo_descricao));
                                }
                            }else {
                                exibirMensagemErro(getString(R.string.preencha_o_campo_telefone));
                            }
                        }else {
                            exibirMensagemErro(getString(R.string.preencha_o_campo_valor));
                        }
                    }else {
                        exibirMensagemErro(getString(R.string.preencha_o_campo_titulo));
                    }
                }else {
                    exibirMensagemErro(getString(R.string.preencha_o_campo_categoria));
                }
            }else {
                exibirMensagemErro(getString(R.string.preencha_o_campo_estado));
            }
        }else {
            exibirMensagemErro(getString(R.string.selecione_pelo_menos_uma_foto));
        }
    }

    private boolean isHasAnImageOrPhoto() {
        return listaFotosRecuperadas != null && (listaFotosRecuperadas.size() != 0 || dadosFoto.length > 0);
    }

    @Override
    public void dismissDiolog() {
        dialog.dismiss();
    }

    @Override
    public void finishActivity() {
        finish();;
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(getApplicationContext(), MeusAnunciosActivity.class));
    }

    @Override
    public void exibirMensagemErro(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Log.d("onClick", "onClick: " + v.getId() );
        switch ( v.getId() ){
            case R.id.imageCadastro1 :
                tirarFoto(SELECAO_CAMERA);
                break;
            case R.id.imageCadastro2 :
                escolherImagem(SELECAO_GALERIA_1);
                break;
            case R.id.imageCadastro3 :
                escolherImagem(SELECAO_GALERIA_2);
                break;
        }
    }

    public void escolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    public void tirarFoto(int requestCode) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Caso seja possível resolver a chamada de abrir a câmera
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, requestCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == Activity.RESULT_OK) {

            if (requestCode != SELECAO_CAMERA) {
                Uri imagemSelecionada = data.getData();
                String caminhoImagem = imagemSelecionada.toString();

                if( requestCode == SELECAO_GALERIA_1 ) {
                    imagem2.setImageURI( imagemSelecionada );

                }else if( requestCode == SELECAO_GALERIA_2 ) {
                    imagem3.setImageURI( imagemSelecionada );
                }

                listaFotosRecuperadas.add( caminhoImagem );

            } else if (requestCode == SELECAO_CAMERA) {

                Bitmap imagem = null;
                imagem = (Bitmap) data.getExtras().get("data");
                imagem1.setImageBitmap(imagem);

                //Recuperar dados da imagem para o firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                dadosFoto = baos.toByteArray();
            }
        }
    }

    private void inicializarComponentes(){

        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        campoTelefone = findViewById(R.id.editTelefone);
        campoEstado = findViewById(R.id.spinnerEstado);
        campoCategoria = findViewById(R.id.spinnerCategoria);
        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);

        //Configura localidade para pt -> portugues BR -> Brasil
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale( locale );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissaoResultado : grantResults ){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permicoes_negadas);
        builder.setMessage(R.string.necessario_aceitar_permissoes);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.confimar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}