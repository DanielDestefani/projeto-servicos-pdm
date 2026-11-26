package com.example.trabalho_andr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtNome;
    private Button btnCliente;
    private Button btnPrestador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtNome = findViewById(R.id.edtNome);
        btnCliente = findViewById(R.id.btnCliente);
        btnPrestador = findViewById(R.id.btnPrestador);

        btnCliente.setOnClickListener(v -> entrar("cliente"));
        btnPrestador.setOnClickListener(v -> entrar("prestador"));
    }

    private void entrar(String perfil) {
        String nome = edtNome.getText().toString().trim();

        if (nome.isEmpty()) {
            Toast.makeText(this, R.string.preencha_nome, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("nome_usuario", nome);
        intent.putExtra("perfil", perfil);
        startActivity(intent);
    }
}
