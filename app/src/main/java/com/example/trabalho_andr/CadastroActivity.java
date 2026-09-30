package com.example.trabalho_andr;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;



public class CadastroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        EditText titulo = findViewById(R.id.editTitulo);
        EditText descricao = findViewById(R.id.editDescricao);
        EditText valor = findViewById(R.id.editValor);
        Button salvar = findViewById(R.id.btnSalvar);

        salvar.setOnClickListener(v -> {
            String editTitulo = titulo.getText().toString();
            String editDescricao = descricao.getText().toString();
            String editValor = valor.getText().toString();

            Intent intentRetorno = new Intent();
            intentRetorno.putExtra("titulo_servico", editTitulo);
            intentRetorno.putExtra("descricao_servico", editDescricao);
            intentRetorno.putExtra("valor_servico", editValor);

            setResult(RESULT_OK, intentRetorno);
            finish();
        });
    }
}