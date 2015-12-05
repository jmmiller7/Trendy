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
 * Credit to Glide examples @ https://github.com/bumptech/glide/blob/master/samples/giphy/src/main/java/com/bumptech/glide/samples/giphy/Api.java
 * Based off of Glide's example class, we've created a smiliar one to handle Imgur's API.
 */
public final class ImgurAPI {
    private static volatile ImgurAPI api = null;
    private static final String CLIENT_ID = "a1b5ac69c0c9d56";
    private static final String BASE_URL = "https://api.giphy.com/v3";
    private static final String SEARCH_PATH = "/gallery/search";
    private static final String TRENDING_PATH = "/gallery/hot/viral/0.json";
    private static Handler bgHandler;
    private static Handler mainHandler;
    private static final HashSet<Monitor> monitors = new HashSet<Monitor>();

    private static String signUrl(String url) {
        return url;
    }

    private static String getSearchUrl(String query) {
        return signUrl(BASE_URL + SEARCH_PATH + "?q=" + query);
    }

    private static String getTrendingUrl() {
        return signUrl(BASE_URL + TRENDING_PATH);
    }

    /**
     * An interface for listening for search results.
     */
    public interface Monitor {
        /**
         * Called when a search completes.
         *
         * @param result The results returned from Imgur's search api.
         */
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
        // Do nothing.
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

    /**
     * A POJO mirroring the top level result JSON object returned from Giphy's api.
     */
    public static class SearchResult {
        public GifResult[] data;

        @Override
        public String toString() {
            return "SearchResult{" + "data=" + Arrays.toString(data) + '}';
        }
    }

    /**
     * A POJO mirroring an individual GIF image returned from Giphy's api.
     */
    public static class GifResult {
        public String id;
        // Page url not gif url
        public String url;
        public GifUrlSet images;

        @Override
        public String toString() {
            return "GifResult{" + "id='" + id + '\'' + ", url='" + url + '\'' + ", images=" + images
                    + '}';
        }
    }

    /**
     * A POJO mirroring a JSON object with a put of urls of different sizes and dimensions returned
     * for a single image from Giphy's api.
     */
    public static class GifUrlSet {
        public GifImage original;
        public GifImage fixed_width;
        public GifImage fixed_height;

        @Override
        public String toString() {
            return "GifUrlSet{" + "original=" + original + ", fixed_width="
                    + fixed_width + ", fixed_height=" + fixed_height
                    + '}';
        }
    }

    /**
     * A POJO mirroring a JSON object for an image with one particular url, size and dimension
     * returned from Giphy's api.
     */
    public static class GifImage {
        public String url;
        public int width;
        public int height;
        public int frames;
        public int size;

        @Override
        public String toString() {
            return "GifImage{" + "url='" + url + '\'' + ", width=" + width + ", height=" + height
                    + ", frames=" + frames + ", size=" + size + '}';
        }
    }
}
