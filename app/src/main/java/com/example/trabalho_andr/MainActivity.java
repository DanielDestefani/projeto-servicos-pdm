package com.example.trabalho_andr;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;



import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewServicos;

    private ActivityResultLauncher<Intent> launcher;

    private ConexaoReceiver conexaoReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        listViewServicos = findViewById(R.id.listViewServicos);

        ArrayList<Servico> listaServicos = new ArrayList<>();

        ServicoAdapter adapter = new ServicoAdapter(this, listaServicos);
        listViewServicos.setAdapter(adapter);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String titulo = data.getStringExtra("titulo_servico");
                        String descricao = data.getStringExtra("descricao_servico");
                        String valorStr = data.getStringExtra("valor_servico");
                        Uri fotoUri = data.getParcelableExtra("imagem_servico");

                        double valor = valorStr != null ? Double.parseDouble(valorStr) : 0;

                        listaServicos.add(new Servico(titulo, descricao, valor, false, fotoUri));
                        adapter.notifyDataSetChanged();
                    }
                }
        );

        Button btnNovoServico = findViewById(R.id.btnNovoServico);
        btnNovoServico.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            launcher.launch(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        conexaoReceiver = new ConexaoReceiver();

        IntentFilter filtro = new IntentFilter();
        filtro.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filtro.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(conexaoReceiver, filtro);

        if (!ConexaoReceiver.temInternet(this)) {
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(conexaoReceiver);
    }
}       // <-- fecha a classe (única chave no fim)
