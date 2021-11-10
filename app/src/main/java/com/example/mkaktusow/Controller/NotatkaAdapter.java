package com.example.mkaktusow.Controller;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotatkaAdapter extends RecyclerView.Adapter {

    List<Notatka> notatki;
    private NotatkaAdapter.OnNotatkaListener mOnNotatkaListener;

    public NotatkaAdapter(List<Notatka> notatki, OnNotatkaListener onNotatkaListener) {
        this.notatki = notatki;
        this.mOnNotatkaListener=onNotatkaListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notatka_row,parent,false);
        View view;
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.notatka_row,parent,false);
            return new ViewHolderZdj(view, mOnNotatkaListener);
        }else{
            view = layoutInflater.inflate(R.layout.notatka_text_row,parent,false);
            return new ViewHolderText(view, mOnNotatkaListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(notatki.get(position).getTypNotatki().equals("tekstowa")||notatki.get(position).getTypNotatki().equals("audio")){
            //bind view holder text
            ViewHolderText viewHolderText = (ViewHolderText) holder;

            viewHolderText.nazwa.setText(notatki.get(position).getNazwaNotatki());
            viewHolderText.typNotatki.setText(notatki.get(position).getTypNotatki());
            if(notatki.get(position).getTypNotatki().equals("tekstowa")){
                viewHolderText.ikona.setImageResource(R.drawable.ic_text_snippet_24);
            }
            if(notatki.get(position).getTypNotatki().equals("audio")){
                viewHolderText.ikona.setImageResource(R.drawable.ic_microphone_24);
            }

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            viewHolderText.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
        }else{
            //bind view holder
            ViewHolderZdj viewHolderZdj = (ViewHolderZdj) holder;
            viewHolderZdj.nazwa.setText(notatki.get(position).getNazwaNotatki());

            viewHolderZdj.typNotatki.setText(notatki.get(position).getTypNotatki());

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            viewHolderZdj.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));

            if (notatki.get(position).getTypNotatki().equals("zdjecie")) {
                viewHolderZdj.previewZdjNotatki.setImageURI(Uri.parse(notatki.get(position).getSciezkaDoZdjecia()));
                viewHolderZdj.videoView.setVisibility(View.GONE);
            }
            if (notatki.get(position).getTypNotatki().equals("film")) {
                viewHolderZdj.videoView.setVisibility(View.VISIBLE);
                viewHolderZdj.previewZdjNotatki.setVisibility(View.GONE);
                viewHolderZdj.videoView.setVideoURI(Uri.parse(notatki.get(position).getSciezkaDoFilmu()));

                viewHolderZdj.videoView.start();
                viewHolderZdj.videoView.pause();
                viewHolderZdj.videoView.seekTo(1);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(notatki.get(position).getTypNotatki().equals("tekstowa")||notatki.get(position).getTypNotatki().equals("audio")){
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return notatki.size();
    }

    public class ViewHolderZdj extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nazwa;
        public TextView typNotatki;
        public TextView datadodania;
        public ImageView previewZdjNotatki;
        public VideoView videoView;
        OnNotatkaListener onNotatkaListener;

        public ViewHolderZdj(View itemView, OnNotatkaListener onNotatkaListener) {
            super(itemView);
            nazwa=itemView.findViewById(R.id.nazwaNotatki_row);
            typNotatki=itemView.findViewById(R.id.typNotatki_row);
            datadodania= itemView.findViewById(R.id.Datadodania_notatki_row);
            previewZdjNotatki = itemView.findViewById(R.id.preview_zdj_notatki_row);
            videoView = itemView.findViewById(R.id.notatka_row_filmview);
            this.onNotatkaListener=onNotatkaListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNotatkaListener.onNotatkaClick(getAdapterPosition());
        }
    }

    public class ViewHolderText extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView nazwa;
        public TextView typNotatki;
        public TextView datadodania;
        public ImageView ikona;
        OnNotatkaListener onNotatkaListener;

        public ViewHolderText(View itemView, OnNotatkaListener onNotatkaListener) {
            super(itemView);
            nazwa=itemView.findViewById(R.id.nazwaNotatki_row);
            typNotatki=itemView.findViewById(R.id.typNotatki_row);
            ikona = itemView.findViewById(R.id.ikona_notatki_tekstowej_row);
            datadodania= itemView.findViewById(R.id.Datadodania_notatki_row);
            this.onNotatkaListener=onNotatkaListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNotatkaListener.onNotatkaClick(getAdapterPosition());
            Toast.makeText(v.getContext(),"a "+getAdapterPosition(), Toast.LENGTH_LONG).show();
        }
    }


    public interface OnNotatkaListener{
        void onNotatkaClick(int position);
    }


}
