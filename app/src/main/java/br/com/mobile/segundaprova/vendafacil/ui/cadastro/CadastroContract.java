package br.com.mobile.segundaprova.vendafacil.ui.cadastro;

public interface CadastroContract {

    interface CadastroView {

        void mostrarToast(String msg);

        void startActivity();

    }

    interface CadastroPresenter {

        void login(String email, String senha);

        void cadastrar(String email, String senha);

    }
}
