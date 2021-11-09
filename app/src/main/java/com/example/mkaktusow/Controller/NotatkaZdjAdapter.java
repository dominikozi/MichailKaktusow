package com.example.mkaktusow.Controller;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotatkaZdjAdapter extends RecyclerView.Adapter<NotatkaZdjAdapter.ViewHolder> {

    List<Notatka> notatki;
    private NotatkaZdjAdapter.OnNotatkaZdjListener mOnNotatkaZdjListener;

    public NotatkaZdjAdapter(List<Notatka> notatki, OnNotatkaZdjListener mOnNotatkaZdjListener) {
        this.notatki = notatki;
        this.mOnNotatkaZdjListener=mOnNotatkaZdjListener;
    }

    @Override
    public NotatkaZdjAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notatka_row,parent,false);
        return new ViewHolder(view, mOnNotatkaZdjListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotatkaZdjAdapter.ViewHolder holder, int position) {
        holder.nazwa.setText(notatki.get(position).getNazwaNotatki());

        holder.typNotatki.setText(notatki.get(position).getTypNotatki());

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));

        if (notatki.get(position).getTypNotatki().equals("zdjecie")) {
            holder.previewZdjNotatki.setImageURI(Uri.parse(notatki.get(position).getSciezkaDoZdjecia()));
        }
        if (notatki.get(position).getTypNotatki().equals("film")) {

        }
        if (notatki.get(position).getTypNotatki().equals("tekstowa")) {
            holder.previewZdjNotatki.setImageResource(R.drawable.ic_text_snippet_24);
        }
        if (notatki.get(position).getTypNotatki().equals("audio")) {
            holder.previewZdjNotatki.setImageResource(R.drawable.ic_microphone_24);
        }

    }
    @Override
    public int getItemCount() {
        return notatki.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nazwa;
        public TextView typNotatki;
        public TextView datadodania;
        public ImageView previewZdjNotatki;
        public TextView previewTresciNotatki;
        OnNotatkaZdjListener onNotatkaZdjListener;

        public ViewHolder(View itemView, OnNotatkaZdjListener OnNotatkaZdjListener) {
            super(itemView);
            nazwa=itemView.findViewById(R.id.nazwaNotatki_row);
            typNotatki=itemView.findViewById(R.id.typNotatki_row);
            datadodania= itemView.findViewById(R.id.Datadodania_notatki_row);
            previewZdjNotatki = itemView.findViewById(R.id.preview_zdj_notatki_row);
            this.onNotatkaZdjListener=OnNotatkaZdjListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNotatkaZdjListener.onNotatkaZdjClick(getAdapterPosition());
            Toast.makeText(v.getContext(),"a "+getAdapterPosition(), Toast.LENGTH_LONG).show();
        }
    }

    public interface OnNotatkaZdjListener{
        void onNotatkaZdjClick(int position);
    }

}
