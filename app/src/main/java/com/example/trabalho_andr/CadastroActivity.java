package com.example.trabalho_andr;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
    }
}