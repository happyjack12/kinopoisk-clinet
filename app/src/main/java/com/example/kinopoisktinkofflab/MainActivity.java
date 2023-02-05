package com.example.kinopoisktinkofflab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    private final String url = "https://kinopoiskapiunofficial.tech/api/v2.2";
    //        private final String key = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b";
//    private final String key = "d43c6ea0-fb36-4ee7-945f-5f92a77c656a";
    private final String key = "d43c6ea0-fb36-4ee7-945f-5f92a77c656a"; // mine
    private ArrayList <HashMap<String, Object>> myMovies;
    private static final String MOVIE = "moviename";
    private static final String DESCRIPTION = "description";
    private static final String ICON = "icon";


    public static Context getmContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().hide();


        Log.d("MYTAG", "EXECUTE");

        List<Film> top = new ArrayList<>();
        AsyncTask<KpApi, Void, List<Film>> myTask = new RetrieveFeedTask();
        try {
            KpApi kpApi = new KpApi(url, key);
            myTask.execute(kpApi);
            top = myTask.get();

            ListView listView = findViewById(R.id.listview);
            int listItemLayout = R.layout.list_item;

            myMovies = new ArrayList<HashMap<String,Object>>();
            HashMap<String, Object> hm;

            ImageView[] imageViews = new ImageView[top.size()];

            for (int i = 1; i < top.size(); i++) {
                hm = new HashMap<String, Object>();
                hm.put(MOVIE, top.get(i).getNameRu());
                hm.put(DESCRIPTION, top.get(i).getDescription());
//                Picasso.get().load(top.get(i).getPosterUrl()).into(imageViews[i]);
//                Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageViews[i]);

//                imageViews[i].setImageDrawable(LoadImageFromWebOperations(top.get(i).getPosterUrl()));
                hm.put(ICON, LoadImageFromWebOperations(top.get(i).getPosterUrl()));
                myMovies.add(hm);
            }

            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    myMovies,
                    listItemLayout, new String[] {
                            MOVIE,
                    DESCRIPTION,
                    ICON
                    }, new int[] {
                            R.id.movie_name,
                    R.id.movie_description,
                    R.id.icon}
            );
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            // TODO
        }
        Log.d("MYTAG", "EXECUTE END");



    }


    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    class RetrieveFeedTask extends AsyncTask<KpApi, Void, List<Film>> {

        @SafeVarargs
        @Override
        protected final List<Film> doInBackground(KpApi... kpApis) {
            try {
                List<Film> top = new ArrayList<>();
                int count = 0;
                for (KpApi kpApi : kpApis) {
                    kpApi.getTopFilms(top, count);
                }
                return top;
            } catch (Exception e) {
                Log.d("MYTAG", "doInBackground: " + e.getMessage());
            }
            return null;
        }

    }

}