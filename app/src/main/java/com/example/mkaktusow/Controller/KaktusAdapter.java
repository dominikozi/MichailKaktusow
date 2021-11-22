package com.example.mkaktusow.Controller;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
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
     //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kaktus_row,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alternatywny_kaktus_row,parent,false);

        return new ViewHolder(view, mOnKaktusListener);
    }

    @Override
    public void onBindViewHolder(@NonNull KaktusAdapter.ViewHolder holder, int position) {
        String nazwakaktusa = kaktusy.get(position).getNazwaKaktusa();
        if(nazwakaktusa.length()>10){
            nazwakaktusa=nazwakaktusa.substring(0,8)+"...";
        }
        holder.nazwaKaktusa.setText(nazwakaktusa);
        String gatunekKaktusa = kaktusy.get(position).getGatunek();
        if(gatunekKaktusa.length()>14){
            gatunekKaktusa=gatunekKaktusa.substring(0,12)+"...";
        }
        holder.gatunek.setText(gatunekKaktusa);
        String nazwaMiesjcaKaktusa = kaktusy.get(position).getNazwaMiejsca();
        if(nazwaMiesjcaKaktusa.length()>14){
            nazwaMiesjcaKaktusa=nazwaMiesjcaKaktusa.substring(0,12)+"...";
        }
        holder.nazwaMiejsca.setText(nazwaMiesjcaKaktusa);
        if(kaktusy.get(position).getSciezkaDoZdjecia()==null){
         //   holder.previewKaktusa.setImageResource(R.drawable.ic_no_image_24);
            holder.previewKaktusa.setImageResource(R.drawable.kaktus);
        }else {
            //Picasso.get().load(notatki.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewZdjNotatki);
            //holder.previewKaktusa.setImageURI(Uri.parse(kaktusy.get(position).getSciezkaDoZdjecia()));
            Picasso.get().load(kaktusy.get(position).getSciezkaDoZdjecia()).resize(180,240).centerCrop().into(holder.previewKaktusa);
        }
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.dataDodaniaK.setText(df.format("yyyy-MM-dd hh:mm",kaktusy.get(position).getDataDodania()));
        if(kaktusy.get(position).getDataOstPodlania()!=null){
            holder.dataOstPodlaniaK.setText(df.format("yyyy-MM-dd hh:mm",kaktusy.get(position).getDataOstPodlania()));
        }else
            holder.dataOstPodlaniaK.setVisibility(View.GONE);
        if(kaktusy.get(position).getDataOstZakwitu()!=null){
            holder.dataOstZakwituK.setText(df.format("yyyy-MM-dd hh:mm",kaktusy.get(position).getDataOstZakwitu()));
        }else
            holder.dataOstZakwituK.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return kaktusy.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        public TextView nazwaKaktusa;
        public TextView gatunek;
        public TextView nazwaMiejsca;
        public TextView dataDodaniaK;
        public TextView dataOstPodlaniaK;
        public TextView dataOstZakwituK;
        public ImageView previewKaktusa;
        OnKaktusListener onKaktusListener;

        CardView cardView;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView, OnKaktusListener onKaktusListener) {
            super(itemView);
        /*    nazwaKaktusa=itemView.findViewById(R.id.nazwaKaktusa_row);
            gatunek=itemView.findViewById(R.id.gatunek_row);
            nazwaMiejsca=itemView.findViewById(R.id.nazwaMiejsca_row);
            previewKaktusa=itemView.findViewById(R.id.preview_zdj_kaktusa_row);
            dataDodaniaK=itemView.findViewById(R.id.datadodania_kaktusa_row);
            this.onKaktusListener=onKaktusListener;
         //   linearLayout = itemView.findViewById(R.id.kaktus_row_Caly_row);
         //   linearLayout.setOnCreateContextMenuListener(this);
            dataOstPodlaniaK=itemView.findViewById(R.id.data_ost_podlania_row);
            dataOstZakwituK=itemView.findViewById(R.id.data_ost_zakwitu_row);
            cardView = itemView.findViewById(R.id.kaktus_row_Caly_row);
            cardView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);*/

            nazwaKaktusa=itemView.findViewById(R.id.alternatywny_nazwa);
            gatunek=itemView.findViewById(R.id.alternatywny_gatunek);
            nazwaMiejsca=itemView.findViewById(R.id.alternatywny_miejsce);
            previewKaktusa=itemView.findViewById(R.id.alternatywny_imageView_zdjecie);
            dataDodaniaK=itemView.findViewById(R.id.alternatywny_datadodania);
            this.onKaktusListener=onKaktusListener;
            //   linearLayout = itemView.findViewById(R.id.kaktus_row_Caly_row);
            //   linearLayout.setOnCreateContextMenuListener(this);
            dataOstPodlaniaK=itemView.findViewById(R.id.alternatywny_data_podlania);
            dataOstZakwituK=itemView.findViewById(R.id.alternatywny_data_kwitniecia);
            constraintLayout = itemView.findViewById(R.id.alternatywny_caly_layout);
            constraintLayout.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onKaktusListener.onKaktusClick(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderIcon(R.drawable.cactus);
            menu.setHeaderTitle("Akcja " + nazwaKaktusa.getText());

            menu.add(this.getAdapterPosition(), 121, 0 , "Usun");
            menu.add(this.getAdapterPosition(), 122, 1 , "Edytuj");

        }
    }

    public interface OnKaktusListener{
        void onKaktusClick(int position);
    }

    public void removeItem(int position){
        kaktusy.remove(position);
        notifyDataSetChanged();
    }

}
