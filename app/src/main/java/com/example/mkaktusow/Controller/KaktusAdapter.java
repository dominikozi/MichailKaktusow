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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kaktus_row,parent,false);
        return new ViewHolder(view, mOnKaktusListener);
    }

    @Override
    public void onBindViewHolder(@NonNull KaktusAdapter.ViewHolder holder, int position) {
        holder.nazwaKaktusa.setText(kaktusy.get(position).getNazwaKaktusa());
        holder.gatunek.setText(kaktusy.get(position).getGatunek());
        holder.nazwaMiejsca.setText(kaktusy.get(position).getNazwaMiejsca());
        if(kaktusy.get(position).getSciezkaDoZdjecia()==null){
            holder.previewKaktusa.setImageResource(R.drawable.ic_no_image_24);
        }else {
            holder.previewKaktusa.setImageURI(Uri.parse(kaktusy.get(position).getSciezkaDoZdjecia()));
        }
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        holder.dataDodaniaK.setText(df.format("yyyy-MM-dd hh:mm",kaktusy.get(position).getDataDodania()));
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

        LinearLayout linearLayout;

        public ViewHolder(View itemView, OnKaktusListener onKaktusListener) {
            super(itemView);
            nazwaKaktusa=itemView.findViewById(R.id.nazwaKaktusa_row);
            gatunek=itemView.findViewById(R.id.gatunek_row);
            nazwaMiejsca=itemView.findViewById(R.id.nazwaMiejsca_row);
            previewKaktusa=itemView.findViewById(R.id.preview_zdj_kaktusa_row);
            dataDodaniaK=itemView.findViewById(R.id.datadodania_kaktusa_row);
            this.onKaktusListener=onKaktusListener;
            linearLayout = itemView.findViewById(R.id.kaktus_row_Caly_row);
            linearLayout.setOnCreateContextMenuListener(this);

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
