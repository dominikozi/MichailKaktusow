package com.example.mkaktusow.View;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Controller.NotatkaZdjAdapter;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragKZdj#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragKZdj extends Fragment  implements NotatkaZdjAdapter.OnNotatkaZdjListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragKZdj() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragKZdj.
     */
    // TODO: Rename and change types and number of parameters
    public static FragKZdj newInstance(String param1, String param2) {
        FragKZdj fragment = new FragKZdj();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JedenKaktusTL activity = (JedenKaktusTL) getActivity();
        Long idKaktusa = activity.getkaktusIDzaktywnosci();

    }

    RecyclerView recyclerViewNotatki;
    RecyclerView.Adapter adapter;
    List<Notatka> notatki;
    List<Notatka> notatkiZdj;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_k_zdj, container, false);

        //recyclerViewNotatki=view.findViewById(R.id.fragkzdj_recycler);
        //recyclerViewNotatki.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //     adapter=new NotatkaZdjAdapter(notatkiZdj, this);
        //    recyclerViewNotatki.setAdapter(adapter);

        JedenKaktusTL activity = (JedenKaktusTL) getActivity();
        notatkiZdj= new ArrayList<Notatka>();
        notatki = activity.getCurrentNotatkiZActivity();
        for( Notatka notatka : notatki){
            if(notatka.getTypNotatki().equals("zdjecie")|| notatka.getTypNotatki().equals("film")){
                notatkiZdj.add(notatka);
            }
        }
        Collections.sort(notatkiZdj,Notatka.NotatkaDataMalejacoComparaotr);

        recyclerViewNotatki=view.findViewById(R.id.fragkzdj_recycler);
        layoutManager=new GridLayoutManager(getContext(),2);
        recyclerViewNotatki.setLayoutManager(layoutManager);
        adapter=new NotatkaZdjAdapter(notatkiZdj, this);
        recyclerViewNotatki.setAdapter(adapter);
        recyclerViewNotatki.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onNotatkaZdjClick(int position) {
        if (notatkiZdj.get(position).getTypNotatki() .equals( "film")) {
            Dialog myDialog = new Dialog(getContext());
            myDialog.setContentView(R.layout.dialog_notatka_film);
            VideoView videoView = (VideoView) myDialog.findViewById(R.id.dialog_notatka_film_filmview);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatka_film_zamknij);
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            videoView.setVideoURI(Uri.parse(notatkiZdj.get(position).getSciezkaDoFilmu()));
            RelativeLayout filmlinearwrapper = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatka_film_filmview_wrapper);
            videoView.seekTo(1);
            myDialog.show();

            filmlinearwrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(videoView.isPlaying()) {
                        videoView.pause();
                    }
                    else {
                        videoView.start();
                    }
                }
            });
            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
        }else if (notatkiZdj.get(position).getTypNotatki() .equals( "zdjecie")) {
            Dialog myDialog = new Dialog(getContext());
            myDialog.setContentView(R.layout.dialog_notatka_zdjecie);
            ImageView imageView = (ImageView) myDialog.findViewById(R.id.dialog_notatkazdjecie_zdjecie);
            Picasso.get().load(notatkiZdj.get(position).getSciezkaDoZdjecia()).fit().centerCrop().into(imageView);
            TextView textView = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_tekst_zawartosc);
            textView.setText(notatkiZdj.get(position).getTrescNotatki());
            textView.setVisibility(View.GONE);
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            TextView dialog_nazwanotatki = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa);
            RelativeLayout relativeLayoutEdytuj = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_edytuj);
            RelativeLayout relativeLayoutAudio = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_audio);
            RelativeLayout relativeLayoutTekst = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_tekst);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_notatkazdjecie_zamknij);
            TextView textView2 = (TextView) myDialog.findViewById(R.id.dialognotatkazdjecie_zobacz_tekst_notatki);

            dialog_nazwanotatki.setText(notatkiZdj.get(position).getNazwaNotatki());
            myDialog.show();

            relativeLayoutEdytuj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), JednaNotatka.class);
                    Long tempId = notatkiZdj.get(position).getIdnotatka();
                    intent.putExtra("id_notatki", tempId);
                    startActivity(intent);
                }
            });
            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });

            relativeLayoutAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if((notatkiZdj.get(position).getSciezkaDoAudio()!=null)&&(!notatkiZdj.get(position).getSciezkaDoAudio().equals(""))){
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(notatkiZdj.get(position).getSciezkaDoAudio());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getContext(), "Brak przypisanego audio",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            relativeLayoutTekst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textView2.getText().equals("ZOBACZ TEKST NOTATKI")){
                        if(notatkiZdj.get(position).getTrescNotatki().length()>0){
                            textView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            textView2.setText("ZOBACZ ZDJECIE");
                        }else{
                            Toast.makeText(getContext(), "Brak przypisanej notatki",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        textView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        textView2.setText("ZOBACZ TEKST NOTATKI");
                    }
                }
            });
        }
    }
}