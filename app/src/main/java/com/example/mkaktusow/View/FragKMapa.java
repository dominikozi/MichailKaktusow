package com.example.mkaktusow.View;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkaktusow.Controller.MapInfoWindowAdapter;
import com.example.mkaktusow.Model.Kaktus;
import com.example.mkaktusow.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragKMapa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragKMapa extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragKMapa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragKMapa.
     */
    // TODO: Rename and change types and number of parameters
    public static FragKMapa newInstance(String param1, String param2) {
        FragKMapa fragment = new FragKMapa();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_k_mapa, null, false);

        JedenKaktusTL activity = (JedenKaktusTL) getActivity();
        Kaktus kaktus = activity.getCurrentKaktusZActivity();
        SupportMapFragment supportMapFragment;
        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                LatLng latLng = new LatLng(kaktus.getLatitude(),kaktus.getLongtitude());
                String snippet = "Gatunek: "+ kaktus.getGatunek() + "!!" + kaktus.getSciezkaDoZdjecia();
                googleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(getContext()));
                //create marker options
                MarkerOptions options = new MarkerOptions().position(latLng).title("Kaktus: " + kaktus.getNazwaKaktusa()).snippet(snippet);
                //zoom camera
           //     MapInfoWindowAdapter mapInfoWindowAdapter = new MapInfoWindowAdapter();
           //     googleMap.setInfoWindowAdapter(mapInfoWindowAdapter);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                //add marker on map
                googleMap.addMarker(options);




            }
        });
        return view;
    }


}