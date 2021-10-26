package com.example.mkaktusow.Controller;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotatkaAdapter extends RecyclerView.Adapter<NotatkaAdapter.ViewHolder> {

    List<Notatka> notatki;

    public NotatkaAdapter(List<Notatka> notatki) {
        this.notatki = notatki;
    }

    @Override
    public NotatkaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notatka_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotatkaAdapter.ViewHolder holder, int position) {
        holder.nazwa.setText(notatki.get(position).getNazwaNotatki());
        holder.typNotatki.setText(notatki.get(position).getTypNotatki());
    }

    @Override
    public int getItemCount() {
        return notatki.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nazwa;
        public TextView typNotatki;
        public ViewHolder(View itemView) {
            super(itemView);
            nazwa=itemView.findViewById(R.id.nazwa_notatka_row);
            typNotatki=itemView.findViewById(R.id.typ_notatki_row);
        }
    }
}
