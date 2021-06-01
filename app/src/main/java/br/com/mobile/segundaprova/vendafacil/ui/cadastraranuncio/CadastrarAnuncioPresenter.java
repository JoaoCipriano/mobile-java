package br.com.mobile.segundaprova.vendafacil.ui.cadastraranuncio;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import br.com.mobile.segundaprova.vendafacil.helper.ConfiguracaoFirebase;
import br.com.mobile.segundaprova.vendafacil.model.Anuncio;

public class CadastrarAnuncioPresenter implements CadastrarAnuncioContract.CadastrarAnunciosPresenter {

    private CadastrarAnuncioContract.CadastrarAnunciosView view;
    private StorageReference storage;
    private Anuncio anuncio;
    private List<String> listaURLFotos = new ArrayList<>();
    private List<String> listaFotosRecuperadas = new ArrayList<>();

    public CadastrarAnuncioPresenter(CadastrarAnuncioContract.CadastrarAnunciosView view) {
        this.view = view;
        storage = ConfiguracaoFirebase.getFirebaseStorage();
    }

    @Override
    public void salvarAnuncio(Anuncio novoAnuncio, List<String> listaFotosRecuperadas, byte[] dadosFoto) {

        this.anuncio = novoAnuncio;
        this.listaFotosRecuperadas = listaFotosRecuperadas;

        /**
         * Salvar foto no Storage
         */
        if (dadosFoto != null && dadosFoto.length > 0)
            salvarFotoStorage(dadosFoto);

        /**
         * Salvar imagem no Storage
         */
        if (listaFotosRecuperadas != null && !listaFotosRecuperadas.isEmpty()) {
            for (int i=0; i < listaFotosRecuperadas.size(); i++){
                String urlImagem = listaFotosRecuperadas.get(i);
                int tamanhoLista = listaFotosRecuperadas.size();
                salvarImagemStorage(urlImagem, tamanhoLista, i);
            }
        }
    }

    private void salvarImagemStorage(String urlString, final int totalFotos, int contador) {

        //Criar nó no storage
        StorageReference imagemAnuncio = storage.child("imagens")
                .child("anuncios")
                .child( anuncio.getIdAnuncio() )
                .child("imagem"+contador);

        //Fazer upload do arquivo
        UploadTask uploadTask = imagemAnuncio.putFile( Uri.parse(urlString) );
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagemAnuncio.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri firebaseUrl = task.getResult();
                        String urlConvertida = firebaseUrl != null ? firebaseUrl.toString() : "";

                        if (!urlConvertida.isEmpty()) {

                            listaURLFotos.add(urlConvertida);

                            if (isFinishedAddImages(totalFotos)) {
                                anuncio.setFotos(listaURLFotos);
                                salvarAnuncio();
                            }
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.exibirMensagemErro("Falha ao fazer upload");
                Log.i("INFO", "Falha ao fazer upload: " + e.getMessage());
            }
        });
    }

    private boolean isFinishedAddImages(int totalFotos) {
        return (totalFotos == listaURLFotos.size()) || totalFotos+1 == listaURLFotos.size();
    }

    private void salvarFotoStorage(byte[] dadosFoto) {

        //Criar nó no storage
        StorageReference fotoAnuncio = storage.child("imagens")
                .child("anuncios")
                .child( anuncio.getIdAnuncio() )
                .child("foto");

        //Fazer upload do arquivo
        UploadTask uploadTask = fotoAnuncio.putBytes( dadosFoto );
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fotoAnuncio.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri firebaseUrl = task.getResult();
                        String urlConvertida = firebaseUrl != null ? firebaseUrl.toString() : "";

                        if (!urlConvertida.isEmpty()) {
                            listaURLFotos.add(urlConvertida);

                            if (listaFotosRecuperadas.isEmpty()) {
                                anuncio.setFotos(listaURLFotos);
                                salvarAnuncio();
                            }
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.exibirMensagemErro("Falha ao fazer upload");
                Log.i("INFO", "Falha ao fazer upload: " + e.getMessage());
            }
        });
    }

    private void salvarAnuncio() {
        anuncio.salvar();

        view.dismissDiolog();
        view.finishActivity();
        view.startActivity();
    }
}
