package com.example.mkaktusow.Controller;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.squareup.picasso.Picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
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
    //    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notatka_row,parent,false);
    //    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alternatywny_notatka_row_zdj,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alt_notatka_zdj_row_grid,parent,false);
        return new ViewHolder(view, mOnNotatkaZdjListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotatkaZdjAdapter.ViewHolder holder, int position) {
     /*   holder.nazwa.setText(notatki.get(position).getNazwaNotatki());

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));

        if (notatki.get(position).getTypNotatki().equals("zdjecie")) {
            wyzerujIkony(holder);
            holder.iv3.setVisibility(View.VISIBLE);
            if(notatki.get(position).getSciezkaDoAudio()!=null && !notatki.get(position).getTrescNotatki().equals("") ){
                holder.iv4.setVisibility(View.VISIBLE);
                holder.iv6.setVisibility(View.VISIBLE);
            }else if(notatki.get(position).getSciezkaDoAudio()!=null && notatki.get(position).getTrescNotatki().equals("") ){
                holder.iv5.setVisibility(View.VISIBLE);
            }
            else if (notatki.get(position).getSciezkaDoAudio()==null && notatki.get(position).getTrescNotatki().length()>0){
                holder.iv4.setVisibility(View.VISIBLE);
            }
            Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewZdjNotatki);
        }
        if (notatki.get(position).getTypNotatki().equals("film")) {
            wyzerujIkony(holder);
            holder.iv2.setVisibility(View.VISIBLE);

            Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewZdjNotatki);
        }*/
        holder.iv2.setVisibility(View.GONE);
        holder.iv3.setVisibility(View.GONE);
        holder.iv4.setVisibility(View.GONE);
        holder.iv5.setVisibility(View.GONE);
        holder.iv6.setVisibility(View.GONE);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));

        if (notatki.get(position).getTypNotatki().equals("film")) {
            holder.iv2.setVisibility(View.VISIBLE);
        }else  if (notatki.get(position).getTypNotatki().equals("zdjecie")) {
            holder.iv3.setVisibility(View.VISIBLE);
            if(notatki.get(position).getSciezkaDoAudio()!=null && !notatki.get(position).getTrescNotatki().equals("")){
                holder.iv5.setVisibility(View.VISIBLE);
                holder.iv6.setVisibility(View.VISIBLE);
            }else if(notatki.get(position).getSciezkaDoAudio()==null && !notatki.get(position).getTrescNotatki().equals("")){
                holder.iv5.setVisibility(View.VISIBLE);
            }else if(notatki.get(position).getSciezkaDoAudio()!=null && notatki.get(position).getTrescNotatki().equals("")){
                holder.iv4.setVisibility(View.VISIBLE);
            }
        }
        Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).fit().centerCrop().into(holder.iv);
    }
    @Override
    public int getItemCount() {
        return notatki.size();
    }
/*
    public void wyzerujIkony(@NonNull NotatkaZdjAdapter.ViewHolder holder){
        holder.iv2.setVisibility(View.GONE);
        holder.iv3.setVisibility(View.GONE);
        holder.iv4.setVisibility(View.GONE);
        holder.iv5.setVisibility(View.GONE);
        holder.iv6.setVisibility(View.GONE);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    /*    public TextView nazwa;
        public TextView typNotatki;
        public TextView datadodania;
        public ImageView previewZdjNotatki;
        public ImageView iv2;
        public ImageView iv3;
        public ImageView iv4;
        public ImageView iv5;
        public ImageView iv6;*/


        public ImageView iv;
        public ImageView iv2;
        public ImageView iv3;
        public ImageView iv4;
        public ImageView iv5;
        public ImageView iv6;
        public TextView data;
        OnNotatkaZdjListener onNotatkaZdjListener;
        public ViewHolder(View itemView, OnNotatkaZdjListener OnNotatkaZdjListener) {
            super(itemView);
            //glowny
      /*    nazwa=itemView.findViewById(R.id.nazwaNotatki_row);
            typNotatki=itemView.findViewById(R.id.typNotatki_row);
            datadodania= itemView.findViewById(R.id.Datadodania_notatki_row);
            previewZdjNotatki = itemView.findViewById(R.id.preview_zdj_notatki_row);
            */
            //

            //alternatywny
       /*     nazwa = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView1);
            datadodania = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView2);
            previewZdjNotatki= itemView.findViewById(R.id.alt_notatka_row_imageView);
            iv2= itemView.findViewById(R.id.alt_notatka_row_imageView2);
            iv3= itemView.findViewById(R.id.alt_notatka_row_imageView3);
            iv4= itemView.findViewById(R.id.alt_notatka_tekst_row_imageView2);
            iv5= itemView.findViewById(R.id.alt_notatka_row_imageView5);  //2-video, 3-image, 4-tekst, 6-audiojeslitekst 5-audiotylkoaudio
            iv6= itemView.findViewById(R.id.alt_notatka_tekst_row_imageView1);
            //*/
            iv= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview);
            iv2= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview2);
            iv3= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview3);
            iv4= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview4);
            iv5= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview5);
            iv6= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview6);
            data= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_textview1);
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
