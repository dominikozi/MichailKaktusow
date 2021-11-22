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

public class NotatkaTextAdapter extends RecyclerView.Adapter<NotatkaTextAdapter.ViewHolder> {

    List<Notatka> notatki;
    private NotatkaTextAdapter.OnNotatkaTextListener mOnNotatkaTextListener;

    public NotatkaTextAdapter(List<Notatka> notatki, OnNotatkaTextListener mOnNotatkaTextListener) {
        this.notatki = notatki;
        this.mOnNotatkaTextListener=mOnNotatkaTextListener;
    }

    @Override
    public NotatkaTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notatka_text_row,parent,false);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alternatywny_notatka_row_text,parent,false);

        return new ViewHolder(view, mOnNotatkaTextListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotatkaTextAdapter.ViewHolder holder, int position) {
     /*   holder.nazwa.setText(notatki.get(position).getNazwaNotatki());
        holder.typNotatki.setText(notatki.get(position).getTypNotatki());
        if(notatki.get(position).getTypNotatki().equals("tekstowa")){
            holder.ikona.setImageResource(R.drawable.ic_text_snippet_24);
        }
        if(notatki.get(position).getTypNotatki().equals("audio")){
            holder.ikona.setImageResource(R.drawable.ic_microphone_24);
        }

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
*/
        holder.ikona.setVisibility(View.GONE);
        holder.ikona2.setVisibility(View.GONE);

        holder.nazwa.setText(notatki.get(position).getNazwaNotatki());
        if(notatki.get(position).getTypNotatki().equals("tekstowa")){
            holder.ikona2.setVisibility(View.VISIBLE);
        }
        if(notatki.get(position).getTypNotatki().equals("audio")){
            holder.ikona.setVisibility(View.VISIBLE);
        }

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
    }
    @Override
    public int getItemCount() {
        return notatki.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nazwa;
        public TextView typNotatki;
        public TextView datadodania;
        public ImageView ikona;
        public ImageView ikona2;
        OnNotatkaTextListener onNotatkaTextListener;

        public ViewHolder(View itemView, OnNotatkaTextListener onNotatkaTextListener) {
            super(itemView);
        /*  nazwa=itemView.findViewById(R.id.nazwaNotatki_row);
            typNotatki=itemView.findViewById(R.id.typNotatki_row);
            datadodania= itemView.findViewById(R.id.Datadodania_notatki_row);
            ikona = itemView.findViewById(R.id.ikona_notatki_tekstowej_row);
*/

            nazwa = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView1);
            datadodania = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView2);
            ikona=itemView.findViewById(R.id.alt_notatka_tekst_row_imageView1);
            ikona2=itemView.findViewById(R.id.alt_notatka_tekst_row_imageView2);

            this.onNotatkaTextListener=onNotatkaTextListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNotatkaTextListener.onNotatkaTextClick(getAdapterPosition());
        }
    }

    public interface OnNotatkaTextListener{
        void onNotatkaTextClick(int position);
    }

}
