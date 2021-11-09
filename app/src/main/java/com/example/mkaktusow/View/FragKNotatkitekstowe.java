package com.example.mkaktusow.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mkaktusow.Controller.NotatkaAdapter;
import com.example.mkaktusow.Controller.NotatkaTextAdapter;
import com.example.mkaktusow.Controller.NotatkaZdjAdapter;
import com.example.mkaktusow.Model.Notatka;
import com.example.mkaktusow.R;

import java.util.ArrayList;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_k_notatkitekstowe, container, false);

        recyclerViewNotatki=view.findViewById(R.id.fragknotatkitekstowe_recycler);
        recyclerViewNotatki.setLayoutManager(new LinearLayoutManager(view.getContext()));
        JedenKaktusTL activity = (JedenKaktusTL) getActivity();
        notatkiText= new ArrayList<Notatka>();
        notatki = activity.getCurrentNotatkiZActivity();
        for( Notatka notatka : notatki){
            if(notatka.getTypNotatki().equals("tekstowa")|| notatka.getTypNotatki().equals("audio")){
                notatkiText.add(notatka);
            }
        }
        adapter=new NotatkaTextAdapter(notatkiText, this);
        recyclerViewNotatki.setAdapter(adapter);

        return view;
    }

    @Override
    public void onNotatkaTextClick(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), JednaNotatka.class);
        Long tempId = notatkiText.get(position).getIdnotatka();
        intent.putExtra("id_notatki",tempId);

        startActivity(intent);
    }
}