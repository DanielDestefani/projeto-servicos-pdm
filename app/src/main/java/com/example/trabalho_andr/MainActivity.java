package com.example.trabalho_andr;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewServicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        listViewServicos = findViewById(R.id.listViewServicos);

        ArrayList<Servico> listaServicos = new ArrayList<>();
        listaServicos.add(new Servico("Troca da resistência do chuveiro",
                "acho que meu chuveiro queimou, não está esquentando a água, preciso trocar a resistência",
                50, false));

        ServicoAdapter adapter = new ServicoAdapter(this, listaServicos);
        listViewServicos.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}