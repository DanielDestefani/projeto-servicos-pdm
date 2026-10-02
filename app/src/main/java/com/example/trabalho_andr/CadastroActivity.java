package com.example.trabalho_andr;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;


public class CadastroActivity extends AppCompatActivity {

    Uri imagemSelecionada;
    ImageView imagem;
    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri ->{
                imagemSelecionada = uri;
                imagem.setImageURI(imagemSelecionada);
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        EditText titulo = findViewById(R.id.editTitulo);
        EditText descricao = findViewById(R.id.editDescricao);
        EditText valor = findViewById(R.id.editValor);
        Button salvar = findViewById(R.id.btnSalvar);
        Button btnImagem = findViewById(R.id.btnImagem);
        imagem = findViewById(R.id.imgPreview);


        salvar.setOnClickListener(v -> {
            String editTitulo = titulo.getText().toString();
            String editDescricao = descricao.getText().toString();
            String editValor = valor.getText().toString();

            Intent intentRetorno = new Intent();
            intentRetorno.putExtra("titulo_servico", editTitulo);
            intentRetorno.putExtra("descricao_servico", editDescricao);
            intentRetorno.putExtra("valor_servico", editValor);
            intentRetorno.putExtra("imagem_servico", imagemSelecionada);

            setResult(RESULT_OK, intentRetorno);
            finish();
        });

        btnImagem.setOnClickListener(v ->{
           launcher.launch("image/*");
        });
    }
}