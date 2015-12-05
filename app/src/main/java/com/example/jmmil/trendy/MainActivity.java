package com.example.jmmil.trendy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;


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
            String query = searchEditText.getText().toString();
            GiphyAPI.search(query);
            ImgurAPI.search(query);
        }
    };

    private void updatePages(GiphyAPI.SearchResult query) {
        if (pagerAdapter != null) {
            Fragment giphy = pagerAdapter.getItem(0);


            // Do stuff
            String[] images = new String[]{
                "http://media1.giphy.com/media/xTiTnybAG7cYyzzvQk/200_d.gif",
                "http://media4.giphy.com/media/3oEduSLalG3rotykI8/200_d.gif",
                "http://media3.giphy.com/media/l41lSxrJMaGyxyBpu/200_d.gif",
                "http://media3.giphy.com/media/n8jpGuNug3LIQ/200_d.gif"
            };
            SimpleGalleryFragment ourFrag = (SimpleGalleryFragment) giphy;
            ourFrag.updateView(images);

            pagerAdapter.replaceItem(0, ourFrag);

            pagerAdapter.notifyDataSetChanged();

            pager.setAdapter(pagerAdapter);


        }

        // Request FragmentManager
        // Detach Fragment by tag
        // Reattach Fragment
        // Ref: http://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
    }

    private void updatePages(ImgurAPI.SearchResult query) {
        if (pagerAdapter != null) {
            Fragment imgur = pagerAdapter.getItem(1);
            //getSupportFragmentManager().beginTransaction().remove(imgur).commit();


        }

        // Request FragmentManager
        // Detach Fragment by tag
        // Reattach Fragment
        // Ref: http://stackoverflow.com/questions/20702333/refresh-fragment-at-reload
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
    // TODO: return tag'd version so you don't have to reference
    // TODO: Refactor this stuff
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
