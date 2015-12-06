package com.example.jmmil.trendy;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import com.astuetz.PagerSlidingTabStrip;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements GiphyAPI.Monitor, ImgurAPI.Monitor {

    private EditText searchEditText;
    private ImageButton searchButton;
    private MyPagerAdapter pagerAdapter;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ViewPager and set an adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        searchEditText = (EditText) findViewById(R.id.tagEditText);
        searchButton = (ImageButton) findViewById(R.id.searchImageButton);
        searchButton.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener(){

        public void onClick(View v) {
            String query = Uri.encode(searchEditText.getText().toString());
            GiphyAPI.search(query);
            ImgurAPI.search(query);
            if (v != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    };

    private void updatePages(GiphyAPI.SearchResult query) {
        if (pagerAdapter != null) {
            Fragment giphy = pagerAdapter.getItem(0);
            int amt = 25;

            String[] images = new String[amt];
            List<String> tmp = new ArrayList<String>();

            for (int i = 0; i < amt-1; i++) {
                GiphyAPI.GifResult q  = query.data[i];
                tmp.add(q.images.fixed_height_downsampled.url);
            }
            images = tmp.toArray(images);

            SimpleGalleryFragment ourFrag = (SimpleGalleryFragment) giphy;
            ourFrag.updateView(images);

            pagerAdapter.replaceItem(0, ourFrag);
            pagerAdapter.notifyDataSetChanged();
            pager.setAdapter(pagerAdapter);
        }
    }

    private void updatePages(ImgurAPI.SearchResult query) {
        if (pagerAdapter != null) {
            Fragment imgur = pagerAdapter.getItem(1);
            int amt = 25;

            String[] images = new String[amt];
            List<String> tmp = new ArrayList<String>();

            for (int i = 0; i < amt-1; i++) {
                ImgurAPI.imageResult q  = query.data[i];
                tmp.add(q.link);
            }
            images = tmp.toArray(images);

            SimpleGalleryFragment ourFrag = (SimpleGalleryFragment) imgur;
            ourFrag.updateView(images);

            pagerAdapter.replaceItem(1, ourFrag);
            pagerAdapter.notifyDataSetChanged();
            pager.setAdapter(pagerAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GiphyAPI.get().addMonitor(this);
        ImgurAPI.get().addMonitor(this);

        if (pagerAdapter != null) {
            if (pagerAdapter.getGalleryItem(0).getImageIDAmt() <= 0
                    && pagerAdapter.getGalleryItem(1).getImageIDAmt() <= 0) {
                GiphyAPI.get().getTrending();
                ImgurAPI.get().getTrending();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        GiphyAPI.get().removeMonitor(this);
        ImgurAPI.get().removeMonitor(this);
    }

    @Override
    public void onSearchComplete(ImgurAPI.SearchResult result) { updatePages(result); }

    @Override
    public void onSearchComplete(GiphyAPI.SearchResult result) { updatePages(result); }
}

class MyPagerAdapter extends FragmentStatePagerAdapter {
    private final String[] TITLES = { "Giphy", "Imgur" };

    ArrayList<SimpleGalleryFragment> fragments;

    public MyPagerAdapter(FragmentManager fm){
        super(fm);
        fragments = new ArrayList<SimpleGalleryFragment>();
        fragments.add(0, new SimpleGalleryFragment());
        fragments.add(1, new SimpleGalleryFragment());
    }

    // This should be okay, not returning a Fragment - considering SimpleGalleryFragment is an
    // `extend`ed version of Fragment
    public SimpleGalleryFragment getGalleryItem(int position) {
        SimpleGalleryFragment fragment = null;

        switch(position) {
            case 0: fragment = fragments.get(0);
                break;
            case 1: fragment = fragments.get(1);
                break;
            default: fragment = new SimpleGalleryFragment();
        }
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        SimpleGalleryFragment fragment;

        switch(position) {
            case 0: fragment = fragments.get(0);
                break;
            case 1: fragment = fragments.get(1);
                break;
            default: fragment = new SimpleGalleryFragment();
        }

        return fragment;
    }


    public void replaceItem(int position, Fragment fragment){
        SimpleGalleryFragment temp = (SimpleGalleryFragment) fragment;
        fragments.set(position, temp);
    }

    @Override
    public CharSequence getPageTitle(int position) { return TITLES[position]; }

    @Override
    public int getCount() { return TITLES.length; }
}
