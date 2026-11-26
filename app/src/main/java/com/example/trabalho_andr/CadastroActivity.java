package com.example.trabalho_andr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.Locale;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    Uri imagemSelecionada;
    ImageView imagem;
    ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                imagemSelecionada = uri;
                imagem.setImageURI(imagemSelecionada);
            }
    );

    private TextView txtCoordenadas;
    private TextView txtStatus;
    private Button btnLocalizacao;
    private FusedLocationProviderClient clienteLocalizacao;
    private double latitude;
    private double longitude;
    private boolean possuiLocalizacao = false;
    private ActivityResultLauncher<String[]> solicitadorPermissoes;

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

        txtCoordenadas = findViewById(R.id.txtCoordenadas);
        txtStatus = findViewById(R.id.txtStatus);
        btnLocalizacao = findViewById(R.id.btnLocalizacao);
        clienteLocalizacao = LocationServices.getFusedLocationProviderClient(this);
        solicitadorPermissoes = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                this::tratarResultadoPermissoes);
        btnLocalizacao.setOnClickListener(v -> verificarPermissoes());

        salvar.setOnClickListener(v -> {
            String editTitulo = titulo.getText().toString();
            String editDescricao = descricao.getText().toString();
            String editValor = valor.getText().toString();

            if (editTitulo.trim().isEmpty() || editValor.trim().isEmpty()) {
                Toast.makeText(this, R.string.preencha_campos, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intentRetorno = new Intent();
            intentRetorno.putExtra("titulo_servico", editTitulo);
            intentRetorno.putExtra("descricao_servico", editDescricao);
            intentRetorno.putExtra("valor_servico", editValor);
            intentRetorno.putExtra("imagem_servico", imagemSelecionada);

            if (possuiLocalizacao) {
                intentRetorno.putExtra("tem_localizacao", true);
                intentRetorno.putExtra("latitude_servico", latitude);
                intentRetorno.putExtra("longitude_servico", longitude);
            }

            setResult(RESULT_OK, intentRetorno);
            finish();
        });

        btnImagem.setOnClickListener(v -> {
            launcher.launch("image/*");
        });
    }

    private boolean temPermissaoLocalizacao() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void verificarPermissoes() {
        if (temPermissaoLocalizacao()) {
            obterLocalizacao();
        } else {
            solicitadorPermissoes.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void tratarResultadoPermissoes(Map<String, Boolean> resultado) {
        if (temPermissaoLocalizacao()) {
            obterLocalizacao();
        } else {
            txtStatus.setText("Permissão de localização não concedida.");
        }
    }

    private void obterLocalizacao() {
        if (!temPermissaoLocalizacao()) {
            txtStatus.setText("Permissão necessária.");
            return;
        }

        txtStatus.setText("Obtendo localização...");
        btnLocalizacao.setEnabled(false);
        CancellationTokenSource cancelamento = new CancellationTokenSource();

        try {
            clienteLocalizacao.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, cancelamento.getToken()
            ).addOnSuccessListener(this, localizacao -> {
                btnLocalizacao.setEnabled(true);
                if (localizacao == null) {
                    txtStatus.setText("Localização indisponível. "
                            + "Verifique o serviço de localização.");
                    return;
                }

                latitude = localizacao.getLatitude();
                longitude = localizacao.getLongitude();
                possuiLocalizacao = true;

                txtCoordenadas.setText(String.format(
                        Locale.getDefault(), "Latitude: %.6f\nLongitude: %.6f",
                        latitude, longitude));
                txtStatus.setText(String.format(
                        Locale.getDefault(), "Precisão estimada: %.1f metros",
                        localizacao.getAccuracy()));
            }).addOnFailureListener(thcleis, erro -> {
                btnLocalizacao.setEnabled(true);
                txtStatus.setText("Erro ao obter localização: " + erro.getMessage());
            });
        } catch (SecurityException e) {
            btnLocalizacao.setEnabled(true);
            txtStatus.setText("Sem autorização para acessar a localização.");
        }
    }
}