package com.example.mkaktusow.Controller;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.squareup.picasso.Picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
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
            Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewZdjNotatki);
        }
        if (notatki.get(position).getTypNotatki().equals("film")) {
            Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewZdjNotatki);
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
   //     public VideoView videoView;
        OnNotatkaZdjListener onNotatkaZdjListener;

        public ViewHolder(View itemView, OnNotatkaZdjListener OnNotatkaZdjListener) {
            super(itemView);
            nazwa=itemView.findViewById(R.id.nazwaNotatki_row);
            typNotatki=itemView.findViewById(R.id.typNotatki_row);
            datadodania= itemView.findViewById(R.id.Datadodania_notatki_row);
            previewZdjNotatki = itemView.findViewById(R.id.preview_zdj_notatki_row);
      //      videoView= itemView.findViewById(R.id.notatka_row_filmview);
            this.onNotatkaZdjListener=OnNotatkaZdjListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNotatkaZdjListener.onNotatkaZdjClick(getAdapterPosition());
        }
    }

    public interface OnNotatkaZdjListener{
        void onNotatkaZdjClick(int position);
    }

    public Bitmap getBitmap(String path) {
        Bitmap bitmap=null;
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }
}
