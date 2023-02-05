package com.example.kinopoisktinkofflab;

import android.util.Log;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class KpApi {
    private String apiUrl;
    private String apiKey;
    private final Integer TOP_FILMS_NUMBER = 10;

    public KpApi(String apiUrl, String key) {
        this.apiUrl = apiUrl;
        this.apiKey = key;
    }

    private HttpURLConnection createRequest(String apiCall) throws IOException {
        URL url = new URL(this.apiUrl + apiCall);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("X-API-KEY", this.apiKey);
        conn.setRequestProperty("Content-Type", "application/json");

        return conn;
    }

    public Film getFilmById(Integer id) {
        try {
            HttpURLConnection connection = createRequest("/films/" + id);

            int resCode = connection.getResponseCode();
            // Log.d("KP_API", "getFilmById(" + id + ") returned " + resCode);
            if (resCode != 200) {
//                System.out.println("resCode != 200 for " + id);
                return null;
            }

//            System.out.println("ENCODING " + connection.getContentEncoding());

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) connection.getContent())); 
            JsonObject rootobj = root.getAsJsonObject();
//            System.out.println("Root: " + rootobj);
            
            Film f = new Film();
            Integer rid = rootobj.get("kinopoiskId").getAsInt();
            f.setId(rid);
            JsonElement name = rootobj.get("nameEn");
            JsonElement nameAlt = rootobj.get("nameOriginal");
//            System.out.println("name: " + name);
            String nameEn;
            if (!name.isJsonNull()) {
//                System.out.println("name != null");
                nameEn = rootobj.get("nameEn").getAsString();
            } else if (!nameAlt.isJsonNull()) {
                nameEn = rootobj.get("nameOriginal").getAsString();
            } else {
                nameEn = "";
            }
            JsonElement nameRu = rootobj.get("nameRu");
            f.setNameEn(nameEn);
            f.setNameRu(!nameRu.isJsonNull() ? nameRu.getAsString() : null);

            JsonElement year = rootobj.get("year");
            f.setYear(!year.isJsonNull() ? year.getAsInt() : null);
    
            JsonElement posterUrl = rootobj.get("posterUrl");
            f.setPosterUrl(!posterUrl.isJsonNull() ? posterUrl.getAsString() : null);
            JsonElement desc = rootobj.get("description");
            f.setDescription(!desc.isJsonNull() ? desc.getAsString() : null);

            JsonArray countries = rootobj.get("countries").getAsJsonArray();
            List<String> cc = new ArrayList<String>();
            for (JsonElement elem : countries) {
//                System.out.println("[country] elem: ");
//                System.out.println(elem);
                JsonObject obj = elem.getAsJsonObject();
//                System.out.println(obj);
                cc.add(obj.get("country").getAsString());
            }
//            System.out.println("cc fill done");
//            System.out.println(cc);
            f.setCountries(cc);

            JsonArray genres = rootobj.get("genres").getAsJsonArray();
            List<String> gg = new ArrayList<String>();
            for (JsonElement elem : genres) {
//                System.out.println("[genre] elem: ");
//                System.out.println(elem);
                JsonObject obj = elem.getAsJsonObject();
//                System.out.println(obj);
                gg.add(obj.get("genre").getAsString());
            }
//            System.out.println("gg fill done");
//            System.out.println(gg);
            f.setGenres(gg);
            
            return f;
        } catch (IOException e) {
//            System.out.println(e.getMessage());
            // Log.d("KP_API", e.getMessage());
        }
        return null;
    }

    public List<Film> getTopFilmsPage(Integer page) {
        try {
            HttpURLConnection connection = createRequest("/films/top?type=TOP_100_POPULAR_FILMS&page=" + page);

            int resCode = 200;
            try {
                resCode = connection.getResponseCode();
            } catch (IOException e) {
                Log.d("MYTAG", "getTopFilmsPage: " + e.getMessage());
            }
            // Log.d("KP_API", "getFilmById(" + id + ") returned " + resCode);
            if (resCode != 200) {
//                System.out.println("resCode != 200 for /films/top");
                Log.d("MYTAG", "Response Code: " + resCode);
                return null;
            }

            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) connection.getContent())); 
            JsonObject rootobj = root.getAsJsonObject();
//            System.out.println("Root: " + rootobj);

            List<Film> list = new ArrayList<>();
            JsonArray arr = rootobj.get("films").getAsJsonArray();
            for (JsonElement film : arr) {
//                System.out.println("[film] " + film);
                JsonObject obj = film.getAsJsonObject();
//                System.out.println("[film->obj] " + obj);
                Integer id = obj.get("filmId").getAsInt();
                Film f = getFilmById(id);
//                System.out.println("[film->getFilmById] " + f);
                list.add(f);
            }

            return list;

        } catch (IOException e) {
//            System.out.println(e.getMessage());
             Log.d("MYTAG", e.getMessage());
        }
        return null;
    }

    public List<Film> getTopFilms(List<Film> top, int gathered) {
//        List<Film> films = new ArrayList<>();
        for (int i = 1; gathered < TOP_FILMS_NUMBER; i++) {
            // films = Stream.concat(films.stream(), getTopFilmsPage(i).stream()).toList();
            top.addAll(getTopFilmsPage(i));
            gathered = top.size();
        }
        return top;
    }
}
