package com.example.mkaktusow.Controller;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        View view;
        LayoutInflater layoutInflater =  LayoutInflater.from(parent.getContext());

        if (viewType == 0) {
        //  view = layoutInflater.inflate(R.layout.alternatywny_notatka_row_zdj,parent,false);
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alt_notatka_zdj_row_grid,parent,false);
            return new ViewHolderZdj(view, mOnNotatkaListener);
        }else{
        //  view = layoutInflater.inflate(R.layout.alternatywny_notatka_row_text,parent,false);
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alt_notatka_text_row_grid,parent,false);
            return new ViewHolderText(view, mOnNotatkaListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(notatki.get(position).getTypNotatki().equals("tekstowa")||notatki.get(position).getTypNotatki().equals("audio")){
       /*
            ViewHolderText viewHolderText = (ViewHolderText) holder;
            viewHolderText.ikona.setVisibility(View.GONE);
            viewHolderText.ikona2.setVisibility(View.GONE);

            viewHolderText.nazwa.setText(notatki.get(position).getNazwaNotatki());
            if(notatki.get(position).getTypNotatki().equals("tekstowa")){
                viewHolderText.ikona2.setVisibility(View.VISIBLE);
            }
            if(notatki.get(position).getTypNotatki().equals("audio")){
                viewHolderText.ikona.setVisibility(View.VISIBLE);
            }

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            viewHolderText.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));*/
            ViewHolderText viewHolderText = (ViewHolderText) holder;
            viewHolderText.ikona.setVisibility(View.GONE);
            viewHolderText.ikona2.setVisibility(View.GONE);
            String nazwa="";

            nazwa = notatki.get(position).getNazwaNotatki();

          //  if(nazwa.length()>35){
        //        nazwa= nazwa.substring(0,35)+"...";
          //  }
            viewHolderText.nazwa.setText(nazwa);

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            viewHolderText.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            if(notatki.get(position).getTypNotatki().equals("tekstowa")){
                viewHolderText.ikona2.setVisibility(View.VISIBLE);
            }
            if(notatki.get(position).getTypNotatki().equals("audio")){
                viewHolderText.ikona.setVisibility(View.VISIBLE);
            }
        }else{

     /*       ViewHolderZdj viewHolderZdj = (ViewHolderZdj) holder;
            viewHolderZdj.nazwa.setText(notatki.get(position).getNazwaNotatki());

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            viewHolderZdj.datadodania.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));

            if (notatki.get(position).getTypNotatki().equals("zdjecie")) {
               wyzerujIkony(viewHolderZdj);
                viewHolderZdj.iv3.setVisibility(View.VISIBLE);
                if(notatki.get(position).getSciezkaDoAudio()!=null && !notatki.get(position).getTrescNotatki().equals("") ){
                    viewHolderZdj.iv5.setVisibility(View.VISIBLE);
                    viewHolderZdj.iv6.setVisibility(View.VISIBLE);
                }else if(notatki.get(position).getSciezkaDoAudio()!=null && notatki.get(position).getTrescNotatki().equals("") ){
                    viewHolderZdj.iv4.setVisibility(View.VISIBLE);
                }
                else if (notatki.get(position).getSciezkaDoAudio()==null && notatki.get(position).getTrescNotatki().length()>0){
                    viewHolderZdj.iv6.setVisibility(View.VISIBLE);
                }
                Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(viewHolderZdj.previewZdjNotatki);
            }
            if (notatki.get(position).getTypNotatki().equals("film")) {
                wyzerujIkony(viewHolderZdj);
                viewHolderZdj.iv2.setVisibility(View.VISIBLE);

                Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(viewHolderZdj.previewZdjNotatki);
            }*/
            ViewHolderZdj viewHolderZdj = (ViewHolderZdj) holder;
            viewHolderZdj.iv2.setVisibility(View.GONE);
            viewHolderZdj.iv3.setVisibility(View.GONE);
            viewHolderZdj.iv4.setVisibility(View.GONE);
            viewHolderZdj.iv5.setVisibility(View.GONE);
            viewHolderZdj.iv6.setVisibility(View.GONE);
            viewHolderZdj.iv11.setVisibility(View.GONE);
            viewHolderZdj.iv12.setVisibility(View.GONE);
            viewHolderZdj.iv13.setVisibility(View.GONE);


            android.text.format.DateFormat df = new android.text.format.DateFormat();
            viewHolderZdj.data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));

            if (notatki.get(position).getTypNotatki().equals("film")) {
                    viewHolderZdj.iv2.setVisibility(View.VISIBLE);
                    viewHolderZdj.iv11.setVisibility(View.VISIBLE);
            }else  if (notatki.get(position).getTypNotatki().equals("zdjecie")) {
                    viewHolderZdj.iv3.setVisibility(View.VISIBLE);
                    viewHolderZdj.iv11.setVisibility(View.VISIBLE);
                    if(notatki.get(position).getSciezkaDoAudio()!=null && !notatki.get(position).getTrescNotatki().equals("")){
                        viewHolderZdj.iv5.setVisibility(View.VISIBLE);
                        viewHolderZdj.iv6.setVisibility(View.VISIBLE);
                        viewHolderZdj.iv13.setVisibility(View.VISIBLE);
                    }else if(notatki.get(position).getSciezkaDoAudio()==null && !notatki.get(position).getTrescNotatki().equals("")){
                        viewHolderZdj.iv5.setVisibility(View.VISIBLE);
                        viewHolderZdj.iv12.setVisibility(View.VISIBLE);

                    }else if(notatki.get(position).getSciezkaDoAudio()!=null && notatki.get(position).getTrescNotatki().equals("")){
                        viewHolderZdj.iv4.setVisibility(View.VISIBLE);
                        viewHolderZdj.iv12.setVisibility(View.VISIBLE);

                    }
                }
                Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).fit().centerCrop().into(viewHolderZdj.iv);

        }
    }
    public void wyzerujIkony(@NonNull ViewHolderZdj holder){
        holder.iv2.setVisibility(View.GONE);
        holder.iv3.setVisibility(View.GONE);
        holder.iv4.setVisibility(View.GONE);
        holder.iv5.setVisibility(View.GONE);
        holder.iv6.setVisibility(View.GONE);
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

    public class ViewHolderZdj extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

      /*  public TextView nazwa;
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

        public ImageView iv11;
        public ImageView iv12;
        public ImageView iv13;

        public TextView data;

        OnNotatkaListener onNotatkaListener;
        ConstraintLayout constraintLayout;
        public ViewHolderZdj(View itemView, OnNotatkaListener onNotatkaListener) {
            super(itemView);

          /*  nazwa = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView1);
            datadodania = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView2);
            previewZdjNotatki= itemView.findViewById(R.id.alt_notatka_row_imageView);
            iv2= itemView.findViewById(R.id.alt_notatka_row_imageView2);
            iv3= itemView.findViewById(R.id.alt_notatka_row_imageView3);
            iv4= itemView.findViewById(R.id.alt_notatka_tekst_row_imageView2);
            iv5= itemView.findViewById(R.id.alt_notatka_row_imageView5);  //2-video, 3-image, 4-tekst, 6-audiojeslitekst 5-audiotylkoaudio
            iv6= itemView.findViewById(R.id.alt_notatka_tekst_row_imageView1);*/

            iv= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview);
            iv2= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview2);
            iv3= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview3);
            iv4= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview4);
            iv5= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview5);
            iv6= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_imageview6);
            data= itemView.findViewById(R.id.alt_notatka_zdj_row_grid_textview1);

            iv11=itemView.findViewById(R.id.alt_notatka_row_bialy_prostokat_1);
            iv12=itemView.findViewById(R.id.alt_notatka_row_bialy_prostokat_2);
            iv13=itemView.findViewById(R.id.alt_notatka_row_bialy_prostokat_3);


            constraintLayout = itemView.findViewById(R.id.alt_notatka_row_zdj_grid_layout);
            constraintLayout.setOnCreateContextMenuListener(this);
            this.onNotatkaListener=onNotatkaListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Akcja ");

            menu.add(this.getAdapterPosition(), 221, 0 , "Usun");

        }

        @Override
        public void onClick(View v) {
            onNotatkaListener.onNotatkaClick(getAdapterPosition());
        }
    }

    public class ViewHolderText extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

    /*    public TextView nazwa;
        public TextView datadodania;
        public ImageView ikona;
        public ImageView ikona2;*/
        ConstraintLayout constraintLayout;
        OnNotatkaListener onNotatkaListener;

        public TextView nazwa;
        public TextView datadodania;
        public ImageView ikona;
        public ImageView ikona2;

        public ViewHolderText(View itemView, OnNotatkaListener onNotatkaListener) {
            super(itemView);
        /*    nazwa = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView1);
            datadodania = itemView.findViewById(R.id.alt_notatka_tekstt_row_textView2);
            ikona=itemView.findViewById(R.id.alt_notatka_tekst_row_imageView1);
            ikona2=itemView.findViewById(R.id.alt_notatka_tekst_row_imageView2);*/
            constraintLayout = itemView.findViewById(R.id.alt_notatka_row_text_grid_layout);
            constraintLayout.setOnCreateContextMenuListener(this);

            nazwa=itemView.findViewById(R.id.alt_notatka_text_row_grid_textview1);

            datadodania= itemView.findViewById(R.id.alt_notatka_text_row_grid_textview3);
            ikona = itemView.findViewById(R.id.alt_notatka_text_row_grid_imageView1);
            ikona2 = itemView.findViewById(R.id.alt_notatka_text_row_grid_imageView2);


            this.onNotatkaListener=onNotatkaListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNotatkaListener.onNotatkaClick(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Akcja " + nazwa.getText());

            menu.add(this.getAdapterPosition(), 221, 0 , "Usun");

        }
    }


    public interface OnNotatkaListener{
        void onNotatkaClick(int position);
    }

    public void removeNotatka(int position){
        notatki.remove(position);
        notifyDataSetChanged();
    }

}
