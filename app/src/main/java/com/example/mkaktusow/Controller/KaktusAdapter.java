package com.example.mkaktusow.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import java.util.List;

public class KaktusAdapter extends RecyclerView.Adapter<KaktusAdapter.ViewHolder>{

    List<Kaktus> kaktusy;

    public KaktusAdapter(List<Kaktus> kaktusy) {
        this.kaktusy = kaktusy;
    }


    @Override
    public KaktusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kaktus_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KaktusAdapter.ViewHolder holder, int position) {
        holder.nazwaKaktusa.setText(kaktusy.get(position).getNazwaKaktusa());
        holder.gatunek.setText(kaktusy.get(position).getGatunek());
        holder.nazwaMiejsca.setText(kaktusy.get(position).getNazwaMiejsca());
    }

    @Override
    public int getItemCount() {
        return kaktusy.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nazwaKaktusa;
        public TextView gatunek;
        public TextView nazwaMiejsca;

        public ViewHolder(View itemView) {
            super(itemView);
            nazwaKaktusa=itemView.findViewById(R.id.nazwaKaktusa_row);
            gatunek=itemView.findViewById(R.id.gatunek_row);
            nazwaMiejsca=itemView.findViewById(R.id.nazwaMiejsca_row);
        }
    }
}
