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
 * Class created by Henry
 *
 * Credit to Glide examples @ https://github.com/bumptech/glide/blob/master/samples/giphy/src/main/java/com/bumptech/glide/samples/giphy/Api.java
 * A java wrapper for Giphy's http api based on https://github.com/Giphy/GiphyAPI.
 */
public final class GiphyAPI {
    private static volatile GiphyAPI api = null;
    private static final String BETA_KEY = "dc6zaTOxFJmzC";
    private static final String BASE_URL = "https://api.giphy.com/";
    private static final String SEARCH_PATH = "v1/gifs/search";
    private static final String TRENDING_PATH = "v1/gifs/trending";
    private static Handler bgHandler;
    private static Handler mainHandler;
    private static final HashSet<Monitor> monitors = new HashSet<Monitor>();

    private static String signUrl(String url) {
        return url + "&api_key=" + BETA_KEY;
    }

    private static String getSearchUrl(String query, int limit, int offset) {
        return signUrl(
                BASE_URL + SEARCH_PATH + "?q=" + query + "&limit=" + limit + "&offset=" + offset);
    }

    private static String getTrendingUrl(int limit, int offset) {
        return signUrl(BASE_URL + TRENDING_PATH + "?limit=" + limit + "&offset=" + offset);
    }

    public static GiphyAPI get() {
        if (api == null) {
            synchronized (GiphyAPI.class) {
                if (api == null) { api = new GiphyAPI(); }
            }
        }
        return api;
    }

    private GiphyAPI() {
        HandlerThread bgThread = new HandlerThread("api_thread");
        bgThread.start();
        bgHandler = new Handler(bgThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public interface Monitor {
        void onSearchComplete(GiphyAPI.SearchResult result);
    }

    public void addMonitor(Monitor monitor) {
        monitors.add(monitor);
    }

    public void removeMonitor(Monitor monitor) {
        monitors.remove(monitor);
    }

    public static void search(String searchTerm) {
        String searchUrl = getSearchUrl(searchTerm, 100, 0);
        query(searchUrl);
    }

    public void getTrending() {
        String trendingUrl = getTrendingUrl(100, 0);
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
        public GifResult[] data;
    }

    public static class GifResult {
        public String id;
        public GifUrlSet images;
    }

    public static class GifUrlSet {
        public GifImage fixed_height_downsampled;
    }

    public static class GifImage {
        public String url;
    }
}
