package com.example.mkaktusow.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Controller.NotatkaTextAdapter;
import com.example.mkaktusow.Controller.NotatkaZdjAdapter;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragKNotatkitekstowe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragKNotatkitekstowe extends Fragment implements NotatkaTextAdapter.OnNotatkaTextListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragKNotatkitekstowe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragKNotatkitekstowe.
     */
    // TODO: Rename and change types and number of parameters
    public static FragKNotatkitekstowe newInstance(String param1, String param2) {
        FragKNotatkitekstowe fragment = new FragKNotatkitekstowe();
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

    RecyclerView recyclerViewNotatki;
    RecyclerView.Adapter adapter;
    List<Notatka> notatki;
    List<Notatka> notatkiText;
    RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_k_notatkitekstowe, container, false);

    //    recyclerViewNotatki=view.findViewById(R.id.fragknotatkitekstowe_recycler);
    //    recyclerViewNotatki.setLayoutManager(new LinearLayoutManager(view.getContext()));
      //  recyclerViewNotatki.setAdapter(adapter);
        JedenKaktusTL activity = (JedenKaktusTL) getActivity();
        notatkiText= new ArrayList<Notatka>();
        notatki = activity.getCurrentNotatkiZActivity();
        for( Notatka notatka : notatki){
            if(notatka.getTypNotatki().equals("tekstowa")|| notatka.getTypNotatki().equals("audio")){
                notatkiText.add(notatka);
            }
        }
        Collections.sort(notatkiText,Notatka.NotatkaDataMalejacoComparaotr);
        //    adapter=new NotatkaTextAdapter(notatkiText, this);

        recyclerViewNotatki=view.findViewById(R.id.fragknotatkitekstowe_recycler);
        layoutManager=new GridLayoutManager(getContext(),2);
        recyclerViewNotatki.setLayoutManager(layoutManager);
        adapter=new NotatkaTextAdapter(notatkiText, this);
        recyclerViewNotatki.setAdapter(adapter);
        recyclerViewNotatki.setHasFixedSize(true);

        return view;
    }
    MediaPlayer mediaPlayer;
    TextView remaining;
    TextView elapsed;
    SeekBar seekbar;
    @Override
    public void onNotatkaTextClick(int position) {
        if (notatkiText.get(position).getTypNotatki() .equals( "audio")) {
            Dialog myDialog = new Dialog(getContext());
            myDialog.setContentView(R.layout.dialog_notatka_audio);
            TextView dialog_nazwanotatki = (TextView) myDialog.findViewById(R.id.dialog_nazwa);
            RelativeLayout imageButton = (RelativeLayout) myDialog.findViewById(R.id.dialog_odtworz_audio);
            RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialog_layout_zamknij);
            dialog_nazwanotatki.setText(notatkiText.get(position).getNazwaNotatki());
            TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
            myDialog.show();
            seekbar = (SeekBar) myDialog.findViewById(R.id.dialog_notatka_audio_seekbar);

            remaining = (TextView) myDialog.findViewById(R.id.dialog_notatka_seekbar1);
            elapsed = (TextView) myDialog.findViewById(R.id.dialog_notatka_seekbar2);
            myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    stopPlaying();
                }
            });
            relativeLayoutZamknij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.hide();
                }
            });
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(notatki.get(position).getSciezkaDoAudio()));
                        mediaPlayer.start();
                        seekbar.setProgress(0);
                        seekbar.setMax(mediaPlayer.getDuration());

                        seekbar.postDelayed(updateSeekBar, 15);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else if (notatkiText.get(position).getTypNotatki() .equals( "tekstowa")) {
                Dialog myDialog = new Dialog(getContext());
                myDialog.setContentView(R.layout.dialog_notatka_tekstowa);
                TextView textView = (TextView) myDialog.findViewById(R.id.dialognotatka_tekst_zawartosc);
                textView.setText(notatkiText.get(position).getTrescNotatki());
                TextView textViewNazwa = (TextView) myDialog.findViewById(R.id.dialognotatka_tekst_nazwa);
                textViewNazwa.setText(notatkiText.get(position).getNazwaNotatki());
                TextView data = (TextView) myDialog.findViewById(R.id.dialog_notatkazdjecie_nazwa2);
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                data.setText(df.format("yyyy-MM-dd hh:mm", notatki.get(position).getDataDodania()));
                RelativeLayout relativeLayoutEdytuj = (RelativeLayout) myDialog.findViewById(R.id.dialognotatka_tekst_edytuj);
                RelativeLayout relativeLayoutZamknij = (RelativeLayout) myDialog.findViewById(R.id.dialognotatka_tekst_zamknij);

                myDialog.show();

                relativeLayoutEdytuj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), JednaNotatka.class);
                        Long tempId = notatkiText.get(position).getIdnotatka();
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
            }
    }

    private Runnable updateSeekBar = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            elapsed.setText(""+ milliSecondsToTimer(totalDuration-currentDuration));
            // Displaying time completed playing
            remaining.setText(""+ milliSecondsToTimer(currentDuration));

            // Updating progress bar
            seekbar.setProgress((int)currentDuration);

            // Call this thread again after 15 milliseconds => ~ 1000/60fps
            seekbar.postDelayed(this, 15);
        }
    };

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10) {
            secondsString = "0" + seconds;
        }else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}