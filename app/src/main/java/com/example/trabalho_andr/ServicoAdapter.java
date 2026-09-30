package com.example.trabalho_andr;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

        Servico servico = servicos.get(position);
        //imageView.setImageResource(servico.getImagem());//
        tituloView.setText(servico.getTitulo());
        descricaoView.setText(servico.getDescricao());
        valorView.setText(String.valueOf(servico.getValor()));

        return rowView;}
}