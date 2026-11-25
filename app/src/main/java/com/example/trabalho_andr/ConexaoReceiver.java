package com.example.trabalho_andr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.widget.Toast;

public class ConexaoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String acao = intent.getAction();

        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(acao)) {
            boolean ligado = intent.getBooleanExtra("state", false);
            String msg = ligado ? "Modo avião ativado" : "Modo avião desativado";
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(acao)) {
            if (temInternet(context)) {
                Toast.makeText(context, "Conexão com a internet restabelecida",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Sem conexão com a internet",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean temInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network rede = cm.getActiveNetwork();
        if (rede == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(rede);
        return caps != null
                && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}