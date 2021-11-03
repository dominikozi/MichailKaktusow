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
    private OnKaktusListener mOnKaktusListener;

    public KaktusAdapter(List<Kaktus> kaktusy, OnKaktusListener onKaktusListener) {
        this.kaktusy = kaktusy;
        this.mOnKaktusListener =onKaktusListener;
    }


    @Override
    public KaktusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kaktus_row,parent,false);
        return new ViewHolder(view, mOnKaktusListener);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nazwaKaktusa;
        public TextView gatunek;
        public TextView nazwaMiejsca;
        OnKaktusListener onKaktusListener;

        public ViewHolder(View itemView, OnKaktusListener onKaktusListener) {
            super(itemView);
            nazwaKaktusa=itemView.findViewById(R.id.nazwaKaktusa_row);
            gatunek=itemView.findViewById(R.id.gatunek_row);
            nazwaMiejsca=itemView.findViewById(R.id.nazwaMiejsca_row);
            this.onKaktusListener=onKaktusListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onKaktusListener.onKaktusClick(getAdapterPosition());
        }
    }

    public interface OnKaktusListener{
        void onKaktusClick(int position);
    }

}
