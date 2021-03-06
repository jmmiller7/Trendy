package com.example.jmmil.trendy;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements GiphyAPI.Monitor, ImgurAPI.Monitor {

    private EditText searchEditText;
    private ImageButton searchButton;
    private MyPagerAdapter pagerAdapter;
    private ViewPager pager;


    // Jake
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
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) v;
                et.setText("");
            }
        });

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditText et = (EditText) v;
                    et.setText("");
                }
            }
        });

        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                    (actionId == EditorInfo.IME_ACTION_DONE)){
                    String query = Uri.encode(searchEditText.getText().toString());
                    GiphyAPI.search(query);
                    ImgurAPI.search(query);
                }

                return false;
            }
        });

        searchButton = (ImageButton) findViewById(R.id.searchImageButton);
        searchButton.setOnClickListener(buttonListener);
    }

    // Henry
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

    // Henry
    private void updatePages(GiphyAPI.SearchResult query) {
        if (pagerAdapter != null) {
            Fragment giphy = pagerAdapter.getItem(0);
            int amt = 22;

            String[] images = new String[amt];
            List<String> tmp = new ArrayList<String>();

            for (int i = 0; i < amt-1; i++) {
                if (i >= query.data.length) break;
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

    // Henry
    private void updatePages(ImgurAPI.SearchResult query) {
        if (pagerAdapter != null) {
            Fragment imgur = pagerAdapter.getItem(1);
            int amt = 22;

            String[] images = new String[amt];
            List<String> tmp = new ArrayList<String>();

            int counter = 0;

            for (int i = 0; i < amt-1; i++) {

                if (counter >= query.data.length) break;

                ImgurAPI.imageResult q  = query.data[counter++];

                if (!q.animated) {
                    tmp.add(q.link);
                }
                else {
                    i--;
                    if (query.data.length + 1 == counter)
                        break;
                }
            }


            images = tmp.toArray(images);

            SimpleGalleryFragment ourFrag = (SimpleGalleryFragment) imgur;
            ourFrag.updateView(images);

            pagerAdapter.replaceItem(1, ourFrag);
            pagerAdapter.notifyDataSetChanged();
            pager.setAdapter(pagerAdapter);
        }
    }

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


// Jake and Henry both contributed
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

    // Jake
    public void replaceItem(int position, Fragment fragment){
        SimpleGalleryFragment temp = (SimpleGalleryFragment) fragment;
        fragments.set(position, temp);
    }

    @Override
    public CharSequence getPageTitle(int position) { return TITLES[position]; }

    @Override
    public int getCount() { return TITLES.length; }
}
