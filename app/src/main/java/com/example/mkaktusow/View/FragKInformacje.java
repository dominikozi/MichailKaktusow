package com.example.mkaktusow.View;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragKInformacje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragKInformacje extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragKInformacje() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragKInformacje.
     */
    // TODO: Rename and change types and number of parameters
    public static FragKInformacje newInstance(String param1, String param2) {
        FragKInformacje fragment = new FragKInformacje();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    TextView nazwaK;
    TextView gatunekK;
    TextView miejsceK;
    TextView data1;
    TextView data2;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JedenKaktusTL activity = (JedenKaktusTL) getActivity();
        Long idKaktusa = activity.getkaktusIDzaktywnosci();
        Kaktus kaktus = activity.getCurrentKaktusZActivity();
        nazwaK = (TextView) view.findViewById(R.id.fragkinfo_nazwa);
        nazwaK.setText(kaktus.getNazwaKaktusa());
        gatunekK = (TextView) view.findViewById(R.id.jedenkaktustl_gatunekkaktusa);
        gatunekK.setText(kaktus.getGatunek());
        miejsceK = (TextView) view.findViewById(R.id.jedenkaktustl_miejsce);
        miejsceK.setText(kaktus.getNazwaMiejsca());
        ImageView zdjK = (ImageView) view.findViewById(R.id.frakinfo_zdjkaktusa);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        data1 = (TextView) view.findViewById(R.id.jedenkaktustl_data1);
        data1.setText("Brak daty ostatniego podlania.");
        data2 = (TextView) view.findViewById(R.id.jedenkaktustl_data2);
        zdjK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Dialog nagDialog = new Dialog(getContext(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(false);
                nagDialog.setContentView(R.layout.popup_full_zdjecie);
                Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                Picasso.get().load(kaktus.getSciezkaDoZdjecia()).into(ivPreview);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();

            }
        });

        if(kaktus.getDataOstPodlania()!=null){
            data1.setText(df.format("yyyy-MM-dd hh:mm", kaktus.getDataOstPodlania()));
        }
        data2.setText("Brak daty ostatniego zakwitu.");

        if(kaktus.getDataOstZakwitu()!=null){
            data2.setText(df.format("yyyy-MM-dd hh:mm", kaktus.getDataOstZakwitu()));
        }

        if(kaktus.getSciezkaDoZdjecia()!=null) {
            Picasso.get().load(kaktus.getSciezkaDoZdjecia()).fit().centerCrop().into(zdjK);
        }else{
            zdjK.setVisibility(View.GONE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_k_informacje, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fragkinfo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JedenKaktusTL activity = (JedenKaktusTL) getActivity();
                Long idKaktusa = activity.getkaktusIDzaktywnosci();
                Intent intent = new Intent(getActivity(), NowaNotatka.class);
                intent.putExtra("idkaktusap",idKaktusa);
                startActivity(intent);
            }
        });



        return view;
    }

}