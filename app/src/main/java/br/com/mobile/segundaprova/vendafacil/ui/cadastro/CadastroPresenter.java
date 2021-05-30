package br.com.mobile.segundaprova.vendafacil.ui.cadastro;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.mobile.segundaprova.vendafacil.helper.ConfiguracaoFirebase;

public class CadastroPresenter implements CadastroContract.CadastroPresenter {

    private FirebaseAuth autenticacao;
    private CadastroContract.CadastroView view;

    public CadastroPresenter(CadastroContract.CadastroView view) {
        this.view = view;
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    }

    @Override
    public void login(String email, String senha) {
        autenticacao.signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                    view.mostrarToast("Logado com sucesso");
                    view.startActivity();
                }else {
                    view.mostrarToast("Erro ao fazer login : " + task.getException());
                }
            }
        });
    }

    @Override
    public void cadastrar(String email, String senha) {
        autenticacao.createUserWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    view.mostrarToast("Cadastro realizado com sucesso!");

                } else {
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Por favor, digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Este conta já foi cadastrada";
                    } catch (Exception e) {
                        erroExcecao = "ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    view.mostrarToast("Erro: " + erroExcecao);
                }
            }
        });
    }
}
