package br.com.mobile.segundaprova.vendafacil.ui.cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.mobile.segundaprova.vendafacil.R;
import br.com.mobile.segundaprova.vendafacil.ui.anuncios.AnunciosActivity;
import br.com.mobile.segundaprova.vendafacil.ui.sobre.SobreActivity;

public class CadastroActivity extends AppCompatActivity implements CadastroContract.CadastroView {

    private Button botaoAcessar;
    private Button botaoSobre;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    private CadastroContract.CadastroPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializaComponentes();
        presenter = new CadastroPresenter(this);

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {

                        if (tipoAcesso.isChecked()) {

                            presenter.cadastrar(email, senha);
                        } else {

                            presenter.login(email, senha);
                        }
                    } else {

                        mostrarToast("Preencha a senha!");
                    }
                } else {

                    mostrarToast("Preencha o E-mail!");
                }
            }
        });

        botaoSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SobreActivity.class);
                startActivity(it);
            }
        });
    }



    private void inicializaComponentes() {
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        botaoSobre = findViewById(R.id.buttonSobre);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }

    @Override
    public void mostrarToast(String msg) {
        Toast.makeText(CadastroActivity.this,
                msg,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(getApplicationContext(), AnunciosActivity.class));
    }
}