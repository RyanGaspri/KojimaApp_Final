package com.example.kojima_transporte_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kojima_transporte_app.model.AtualizarDados;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    ArrayList id, motorista, veiculo, data,placa,km_saida,km_final,hora_final,hora_inicial,
            data_final,data_inicial,pernoite, chegada, saidaDoCliente, destino, saidaDestino,horaChegada,
    horaSaida, horaDestino, dataSaida, observacoes;

    CustomAdapter(Activity activity,Context context,
                  ArrayList id,
                  ArrayList motorista,
                  ArrayList veiculo,
                  ArrayList data,
                  ArrayList placa,
                  ArrayList km_saida,
                  ArrayList km_final,
                  ArrayList hora_final,
                  ArrayList hora_inicial,
                  ArrayList data_final,
                  ArrayList data_inicial,
                  ArrayList pernoite,
                  ArrayList chegada,
                  ArrayList saidaDoCliente,
                  ArrayList destino,
                  ArrayList saidaDestino,
                  ArrayList horaChegada,
                  ArrayList horaSaida,
                  ArrayList horaDestino,
                  ArrayList dataSaida,
                  ArrayList observacoes
    ){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.veiculo = veiculo;
        this.motorista = motorista;
        this.data = data;
        this.placa = placa;
        this.km_saida = km_saida;
        this.km_final = km_final;
        this.hora_final = hora_final;
        this.hora_inicial = hora_inicial;
        this.data_final = data_final;
        this.data_inicial = data_inicial;
        this.pernoite = pernoite;
        this.chegada = chegada;
        this.saidaDoCliente = saidaDoCliente;
        this.destino = destino;
        this.saidaDestino = saidaDestino;
        this.horaChegada = horaChegada;
        this.horaSaida = horaSaida;
        this.horaDestino = horaDestino;
        this.dataSaida = dataSaida;
        this.observacoes = observacoes;


    }
    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.id.setText(String.valueOf(id.get(position)));
        holder.motorista.setText(String.valueOf(motorista.get(position)));
        holder.veiculo.setText(String.valueOf(veiculo.get(position)));
        holder.data.setText(String.valueOf(data.get(position)));
        holder.cardOrdem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AtualizarDados.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                intent.putExtra("motorista", String.valueOf(motorista.get(position)));
                intent.putExtra("veiculo", String.valueOf(veiculo.get(position)));
                intent.putExtra("data", String.valueOf(data.get(position)));
                intent.putExtra("placa", String.valueOf(placa.get(position)));
                intent.putExtra("kmsaida", String.valueOf(km_saida.get(position)));
                intent.putExtra("kmfinal", String.valueOf(km_final.get(position)));
                intent.putExtra("datainicial", String.valueOf(data_inicial.get(position)));
                intent.putExtra("datafinal", String.valueOf(data_final.get(position)));
                intent.putExtra("horainicial", String.valueOf(hora_inicial.get(position)));
                intent.putExtra("horafinal", String.valueOf(hora_final.get(position)));
                intent.putExtra("pernoites", String.valueOf(pernoite.get(position)));
                intent.putExtra("chegada", String.valueOf(chegada.get(position)));
                intent.putExtra("saidaDoCliente", String.valueOf(saidaDoCliente.get(position)));
                intent.putExtra("destino", String.valueOf(destino.get(position)));
                intent.putExtra("saidaDestino", String.valueOf(saidaDestino.get(position)));
                intent.putExtra("horaChegada", String.valueOf(horaChegada.get(position)));
                intent.putExtra("horaSaida", String.valueOf(horaSaida.get(position)));
                intent.putExtra("horaDestino", String.valueOf(horaDestino.get(position)));
                intent.putExtra("dataDestino", String.valueOf(dataSaida.get(position)));
                intent.putExtra("observacoes", String.valueOf(observacoes.get(position)));

                activity.startActivityForResult(intent,1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, motorista, veiculo, data;
        CardView cardOrdem;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            id = itemView.findViewById(R.id.txt_id);
            motorista = itemView.findViewById(R.id.txt_Motorista);
            veiculo = itemView.findViewById(R.id.txt_veiculo);
            data = itemView.findViewById(R.id.txt_data);
            cardOrdem = itemView.findViewById(R.id.cardOrdem);
        }
    }
}
