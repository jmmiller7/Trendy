package com.example.jmmil.trendy;

import com.google.gson.Gson;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Henry
 *
 * Credit to Glide examples @ https://github.com/bumptech/glide/blob/master/samples/giphy/src/main/java/com/bumptech/glide/samples/giphy/Api.java
 * Based off of Glide's example class, we've created a smiliar one to handle Imgur's API.
 */
public final class ImgurAPI {
    private static volatile ImgurAPI api = null;
    private static final String CLIENT_ID = "a1b5ac69c0c9d56";
    private static final String BASE_URL = "https://api.imgur.com/3/";
    private static final String SEARCH_PATH = "gallery/search";
    private static final String TRENDING_PATH = "gallery/hot/viral";
    private static Handler bgHandler;
    private static Handler mainHandler;
    private static final HashSet<Monitor> monitors = new HashSet<Monitor>();

    private static String signUrl(String url) {
        return url;
    }

    private static String getSearchUrl(String query) {
        return signUrl(BASE_URL + SEARCH_PATH + "?q=" + query + "&q_size_px=500&q_type=jpg");
    }

    private static String getTrendingUrl() {
        return signUrl(BASE_URL + TRENDING_PATH + "&q_size_px=500&q_type=jpg");
    }

    public interface Monitor {
        void onSearchComplete(SearchResult result);
    }

    public static ImgurAPI get() {
        if (api == null) {
            synchronized (ImgurAPI.class) {
                if (api == null) { api = new ImgurAPI(); }
            }
        }
        return api;
    }

    private ImgurAPI() {
        HandlerThread bgThread = new HandlerThread("api_thread");
        bgThread.start();
        bgHandler = new Handler(bgThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void addMonitor(Monitor monitor) {
        monitors.add(monitor);
    }

    public void removeMonitor(Monitor monitor) {
        monitors.remove(monitor);
    }

    public static void search(String searchTerm) {
        String searchUrl = getSearchUrl(searchTerm);
        query(searchUrl);
    }

    public void getTrending() {
        String trendingUrl = getTrendingUrl();
        query(trendingUrl);
    }

    private static void query(final String apiUrl) {
        bgHandler.post(new Runnable() {
            @Override
            public void run() {
            URL url;
            try {
                url = new URL(apiUrl);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            HttpURLConnection urlConnection = null;
            InputStream is = null;
            SearchResult result = new SearchResult();
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
                is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                result = new Gson().fromJson(reader, SearchResult.class);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // Do nothing.
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            final SearchResult finalResult = result;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                for (Monitor monitor : monitors) {
                    monitor.onSearchComplete(finalResult);
                }
                }
            });
            }
        });
    }

    public static class SearchResult {
        public imageResult[] data;
    }

    public static class imageResult {
        public String id;
        public String link;
        public boolean animated;
    }

}
