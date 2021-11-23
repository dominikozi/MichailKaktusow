package com.example.mkaktusow.View;
import com.bumptech.glide.Glide;
import com.example.mkaktusow.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class Encyklopedia extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    ImageView imageView;
    ImageView imageView2;
    TextView source;
    List<String> img;
    List<String> img2;

    Spinner spinnerGatunek;
    scrape s;
    String link = null;

    GifImageView gifImageView;

    List<String> gatunkiKaktusow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyklopedia);
        gifImageView=findViewById(R.id.encyklopedia_gif);
        //spinner gatunek
        spinnerGatunek = findViewById(R.id.encyklopedia_spinner_gatunek);
        textView = findViewById(R.id.encyklopedia_tekst);
        textView2 = findViewById(R.id.encyklopedia_tekst2);
        textView3 = findViewById(R.id.encyklopedia_tekst3);
        textView4 = findViewById(R.id.encyklopedia_tekst4);
        textView5 = findViewById(R.id.encyklopedia_tekst5);
        textView6 = findViewById(R.id.encyklopedia_tekst6);
        textView7 = findViewById(R.id.encyklopedia_tekst7);
        imageView = findViewById(R.id.encyklopedia_zdjecie);
        imageView2 = findViewById(R.id.encyklopedia_zdjecie2);
        source = findViewById(R.id.encyklopedia_z_jakiej_strony);
//----bottom navigation bar
        //Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set encyklopedia selected
        bottomNavigationView.setSelectedItemId(R.id.Encyklopedia);
        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.Encyklopedia:
                    return true;
                case R.id.Notatki:
                    startActivity(new Intent(getApplicationContext(), Notatki.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.Miejsca:
                    startActivity(new Intent(getApplicationContext(), Kaktusy.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.Mapa:
                    startActivity(new Intent(getApplicationContext(), Mapa.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;

        });
        if(!isNetworkAvailable()){
            source.setText("Brak połączenia z internetem. Połącz się z internetem by skorzystać z funkcjonalności.");
            textView.setText("");
            textView2.setText("");
            textView3.setText("");
            textView4.setText("");
            gifImageView.setVisibility(View.GONE);
            textView5.setText("");
            textView6.setText("");
            textView7.setText("");
            imageView2.setVisibility(View.GONE);
            spinnerGatunek.setVisibility(View.GONE);
            Toast.makeText(this, "Brak połączenia z internetem", Toast.LENGTH_SHORT).show();
        }else {

            gatunkiKaktusow = new ArrayList<String>();
            gatunkiKaktusow.add("Opuncja drobnokolczasta");
            gatunkiKaktusow.add("Opuncja figowa");
            gatunkiKaktusow.add("Wielomlecz trójżebrowy");
            gatunkiKaktusow.add("Cereus repandus");
            gatunkiKaktusow.add("Echinokaktus Grusonii");
            gatunkiKaktusow.add("Echinopsis Eyriesa");
            gatunkiKaktusow.add("Echinopsis spachiana");
            gatunkiKaktusow.add("Mammillaria Haw.");
            gatunkiKaktusow.add("Jazgrza Williamsa");
            gatunkiKaktusow.add("Ferokaktus");
            gatunkiKaktusow.add("Gymnocalycium Monvillei");



            ArrayAdapter<String> spinnerGatunekAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gatunkiKaktusow);
            spinnerGatunekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGatunek.setAdapter(spinnerGatunekAdapter);
            spinnerGatunek.setOnItemSelectedListener(this);




            WyswietlLoading();

            source.setText("Źródło: fajnyogrod.pl");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        WyswietlLoading();
        switch(parent.getItemAtPosition(position).toString()){
            case "Opuncja drobnokolczasta":
                link = "https://fajnyogrod.pl/porady/opuncja-drobnokolczasta-uprawa-pielegnacja-podlewanie-wymagania/";
                source.setText("Źródło: https://fajnyogrod.pl/");
                s = new scrape();
                s.execute();
                break;
            case "Opuncja figowa":
                link = "https://fajnyogrod.pl/porady/opuncja-figowa-uprawa-hodowla-rozmnazanie-podlewanie-porady/";
                source.setText("Źródło: https://fajnyogrod.pl/");
                s = new scrape();
                s.execute();
                break;
            case "Wielomlecz trójżebrowy":
                link = "https://fajnyogrod.pl/porady/wilczomlecz-trojzebrowy-sadzenie-uprawa-rozmnazanie-przycinanie/";
                source.setText("Źródło: https://fajnyogrod.pl/");
                s = new scrape();
                s.execute();
                break;
            case "Cereus repandus":
                link = "https://fajnyogrod.pl/porady/cereus-repandus-w-doniczce-uprawa-pielegnacja-ciekawostki/";
                source.setText("Źródło: https://fajnyogrod.pl/");
                s = new scrape();
                s.execute();
                break;
            case "Echinokaktus Grusonii":
                link = "https://fajnyogrod.pl/porady/echinocactus-grusonii-w-doniczce-pielegnacja-podlewanie-uprawa/";
                source.setText("Źródło: https://fajnyogrod.pl/");
                s = new scrape();
                s.execute();
                break;
            case "Echinopsis Eyriesa":
            case "Gymnocalycium Monvillei":
            case "Ferokaktus":
            case "Mammillaria Haw.":
            case "Echinopsis spachiana":
            case "Jazgrza Williamsa":
                wikipediaS wikipedias = new wikipediaS();
                wikipedias.execute(parent.getItemAtPosition(position).toString());
         //       getDataFromWikipediaAPI(parent.getItemAtPosition(position).toString());
                source.setText("Źródło: https://pl.wikipedia.org/");
                break;
            default:
                Toast.makeText(getApplicationContext(),"xd",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class scrape extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            img = new ArrayList<String>();
            img2 = new ArrayList<String>();

            //inicjalizacja dokumentu
            org.jsoup.nodes.Document document = null;

            try {

               // document = Jsoup.connect("https://fajnyogrod.pl/porady/wilczomlecz-trojzebrowy-sadzenie-uprawa-rozmnazanie-przycinanie/").get();
               // document = Jsoup.connect("https://fajnyogrod.pl/porady/opuncja-drobnokolczasta-uprawa-pielegnacja-podlewanie-wymagania/").get();
               // document = Jsoup.connect("https://fajnyogrod.pl/porady/opuncja-figowa-uprawa-hodowla-rozmnazanie-podlewanie-porady/").get();
                document = Jsoup.connect(link).get();

            } catch (IOException e) {
                e.printStackTrace();
            }
            document.select(".small-italic").remove();
            org.jsoup.select.Elements naglowek = document.getElementsByClass("text-heading text-heading-h1");
            org.jsoup.select.Elements Articlelead3 = document.getElementsByClass("article-lead");

            org.jsoup.select.Elements Articlelead = document.getElementsByClass("main-col");
            org.jsoup.select.Elements Articlelead2 = document.getElementsByTag("p");
            org.jsoup.select.Elements zdj2 = document.getElementsByClass("mi");


            List<String> tekst = new ArrayList<String>();
            int ij=0;
            for( Element i : Articlelead2){
                String im = i.toString();

                tekst.add(im);

                ij++;
                System.out.println(ij);
                System.out.println(im);
            }

            for(Element i : Articlelead) {
                String im = i.getElementsByTag("img").attr("src");

                img.add(im);
              System.out.println(im);
            }
            for(Element i : zdj2) {
                String im = i.getElementsByTag("img").attr("src");

                img2.add(im);
                System.out.println(im);
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    imageView.setVisibility(View.VISIBLE);
                    gifImageView.setVisibility(View.GONE);
                    textView.setText(naglowek.text());
                    textView2.setText(html2text(Articlelead3.text()));
                    textView3.setText(html2text(tekst.get(2)));
                    StringBuilder builder = new StringBuilder();
                    tekst.remove(1);tekst.remove(2);tekst.remove(3);
                    for (String details : tekst) {
                        if(html2text(details).trim().length() > 0 ){
                            builder.append(html2text(details) + "\n");
                        }
                    }
                    textView4.setText(builder.toString());
              //      Glide.with(Encyklopedia.this).load(img.get(1)).into(imageView);
                    Picasso.get().load(img.get(1)).into(imageView);

                    try {
           //             Glide.with(Encyklopedia.this).load(img2.get(0)).into(imageView2);
                        if(imageView2.getVisibility()==View.GONE){
                            imageView2.setVisibility(View.VISIBLE);
                        }
                        Picasso.get().load(img2.get(0)).into(imageView2);
                    }catch(Exception e){

                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    private class wikipediaS extends AsyncTask<String,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            String querry = params[0];
            String linkgoogle = null;
            try {
                linkgoogle = "https://www.google.com/search?q=" + URLEncoder.encode(querry, "UTF-8");
                Document google = Jsoup.connect(linkgoogle).ignoreHttpErrors
                        (true).timeout(0).get();
                Elements searchLinks = google.getElementsByTag("cite");

//                if (searchLinks.isEmpty()) {
//                    //co jesli nei ma takiego wyszukania w wikipedii
//
//                }

                String searchLink = searchLinks.get(0).text();
                if(searchLink.contains("›")) {
                    searchLink = searchLink.replaceAll(" › ", "/");
                }
          //      System.out.println(searchLink);

                if(searchLink.contains("...")){
                    searchLink = "https://pl.wikipedia.org/wiki/" + params[0];
                }
    //POBIERANIE ZDJĘCIA
                String[] parts = searchLink.split("/");
                String title = parts[parts.length-1];
                //https://pl.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&titles=opuncja  <- zdjecie główne
                String zdjecie = "https://pl.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&titles="+title;
          //      System.out.println(title);
                String json = Jsoup.connect(zdjecie).ignoreContentType(true).execute().body();
          //      System.out.println(json);
                JSONObject jObject = new JSONObject(json);
                //!-json
                JSONObject jObject2 = jObject.getJSONObject("query").getJSONObject("pages");
                String jsonTemp = jObject2.toString();
                jsonTemp = jsonTemp.substring(0,10).replaceAll("[^a-zA-Z0-9]", "");  ;
         //       System.out.println(jsonTemp);
         //       System.out.println(jObject2);

                JSONObject jObject3 = jObject2.getJSONObject(jsonTemp).getJSONObject("original");
          //      System.out.println(jObject3);
                String linkdozdj = jObject3.getString("source");
          //      System.out.println(linkdozdj);
                title = title.substring(0, 1).toUpperCase() + title.substring(1);
                title = title.replaceAll("_", " ");
                String finalTitle = title;

    //POBIERANIE TEKSTU

                //searchLink
                String tekst = "https://pl.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&explaintext=&titles="+title;
                String json2 = Jsoup.connect(tekst).ignoreContentType(true).execute().body();
                JSONObject jObjectT = new JSONObject(json2);
         //       System.out.println(jsonTemp);
                JSONObject jObjectT2 = jObjectT.getJSONObject("query").getJSONObject("pages");
         //       System.out.println(jObjectT2);
                JSONObject jObjectT3 = jObjectT2.getJSONObject(jsonTemp);
         //       System.out.println(jObjectT3);
                String tekstExtract = jObjectT3.getString("extract");
                int itetarotr=0;
                while(tekstExtract.contains("==")){
                    if(itetarotr%2==1){
                        tekstExtract=tekstExtract.replaceFirst("=="," <br> <br> </h2>");
                    }else{
                        tekstExtract=tekstExtract.replaceFirst("==","<h2> <br> <br>");
                    }
                    itetarotr++;
                }

                tekstExtract.replaceAll("=","");

                String finalTekstExtract = tekstExtract;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.VISIBLE);
                        textView.setText(finalTitle);
                        textView2.setText(Html.fromHtml(finalTekstExtract));
                        textView3.setText("");
                        textView4.setText("");
                        textView5.setText("");
                        textView6.setText("");
                        textView7.setText("");
                        gifImageView.setVisibility(View.GONE);
                        Picasso.get().load(linkdozdj).into(imageView);
                        if(imageView2.getVisibility()==View.VISIBLE){
                            imageView2.setVisibility(View.GONE);
                        }
                    }
                });



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public void WyswietlLoading(){
        textView.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");
        textView5.setText("");
        textView6.setText("");
        textView7.setText("");
        imageView.setVisibility(View.GONE);
        gifImageView.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.GONE);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}