package com.example.trabalho_andr;

import android.content.ActivityNotFoundException;
import android.widget.Button;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.InputStream;
import java.util.ArrayList;

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

        if (servico.isAceito()) {
            btnAceitar.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
            btnAceitar.setImageResource(android.R.drawable.ic_input_add);
        }

        btnAceitar.setOnClickListener(v -> {
            if (servico.isAceito()) {
                Toast.makeText(context, R.string.servico_ja_aceito, Toast.LENGTH_SHORT).show();
                return;
            }
            servico.setAceito(true);
            notifyDataSetChanged();
            Toast.makeText(context,
                    context.getString(R.string.servico_aceito_toast, servico.getTitulo()),
                    Toast.LENGTH_SHORT).show();
            showNotification(servico);
        });

        btnApagar.setOnClickListener(v -> {
            servicos.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, R.string.item_removido, Toast.LENGTH_SHORT).show();
        });

        configurarMapa(rowView, servico);

        return rowView;
    }

    private void showNotification(Servico servico) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String channelId = "canal_servicos";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    context.getString(R.string.notif_canal),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(context.getString(R.string.notif_titulo))
                .setContentText(context.getString(R.string.notif_texto, servico.getTitulo()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Bitmap foto = carregarFoto(servico.getFotoUri());
        if (foto != null) {
            builder.setLargeIcon(foto);
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        int notificationId = System.identityHashCode(servico);
        notificationManager.notify(notificationId, builder.build());
    }

    private Bitmap carregarFoto(Uri uri) {
        if (uri == null) {
            return null;
        }
        try (InputStream in = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.Options opcoes = new BitmapFactory.Options();
            opcoes.inSampleSize = 4;
            return BitmapFactory.decodeStream(in, null, opcoes);
        } catch (Exception e) {
            return null;
        }
    }
    private void configurarMapa(View rowView, Servico servico) {
        Button btnMapa = rowView.findViewById(R.id.btnMapa);

        if (!servico.temLocalizacao()) {
            btnMapa.setVisibility(View.GONE);
            return;
        }

        btnMapa.setVisibility(View.VISIBLE);
        btnMapa.setOnClickListener(v -> abrirMapa(servico));
    }

    private void abrirMapa(Servico servico) {
        double lat = servico.getLatitude();
        double lng = servico.getLongitude();

        Uri uri = Uri.parse(
                "geo:" + lat + "," + lng
                        + "?q=" + lat + "," + lng
                        + "(" + Uri.encode(servico.getTitulo()) + ")"
        );
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Nenhum aplicativo de mapas disponível.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
