package com.example.trabalho_andr;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ServicoAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Servico> servicos;

    public ServicoAdapter(Context context, ArrayList<Servico> servicos) {
        this.context = context;
        this.servicos = servicos;
    }

    @Override
    public int getCount() {
        return servicos.size();
    }

    @Override
    public Object getItem(int position) {
        return servicos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.list_item_servico, parent, false);

        ImageView imageView = rowView.findViewById(R.id.icone_servico);
        TextView tituloView = rowView.findViewById(R.id.titulo_servico);
        TextView descricaoView = rowView.findViewById(R.id.descricao_servico);
        TextView valorView = rowView.findViewById(R.id.valor_servico);
        ImageButton btnAceitar = rowView.findViewById(R.id.btnAceitar);
        ImageButton btnApagar = rowView.findViewById(R.id.btnApagar);

        Servico servico = servicos.get(position);
        imageView.setImageURI(servico.getFotoUri());
        tituloView.setText(servico.getTitulo());
        descricaoView.setText(servico.getDescricao());
        valorView.setText(String.valueOf(servico.getValor()));

        btnAceitar.setOnClickListener(v -> {
            servico.setAceito(true);
        Toast.makeText(context, "Serviço aceito: " + servico.getTitulo(), Toast.LENGTH_SHORT).show();
            showNotification(servico.getTitulo());
        });

        btnApagar.setOnClickListener(v -> {
            servicos.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Item removido", Toast.LENGTH_SHORT).show();
        });

        return rowView;}

    private void showNotification(String titulo) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String channelId = "canal_servicos";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Serviços",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Serviço aceito")
                .setContentText("O prestador aceitou: " + titulo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}