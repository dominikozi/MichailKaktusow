package com.example.mkaktusow.Controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mkaktusow.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public MapInfoWindowAdapter(Context context) {
        this.mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.map_info_window_custom, null);
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        String title = marker.getTitle();
        TextView tvTitle = (TextView) mWindow.findViewById(R.id.infowindow_nazwa);

        if(!title.equals("")){
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        String[] dane = snippet.split("!!");

        String gatunek = dane[0];
        TextView tvGatunek = (TextView) mWindow.findViewById(R.id.infowindow_gatunek);

        if(!title.equals("")){
            tvGatunek.setText(gatunek);
        }

        String zdj = dane[1];
        ImageView ivZdjecie = (ImageView) mWindow.findViewById(R.id.infowindow_zdjecie);

        if(!title.equals("")){
            Picasso.get().load(zdj).resize(180,240).centerCrop().into(ivZdjecie, new MarkerCallback(marker));
        }
        return mWindow;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    public class MarkerCallback implements Callback {
        Marker marker=null;

        public MarkerCallback(Marker marker) {
            this.marker = marker;
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }

        @Override
        public void onError(Exception e) {
            Log.e(getClass().getSimpleName(), "Error");
        }
    }
}
