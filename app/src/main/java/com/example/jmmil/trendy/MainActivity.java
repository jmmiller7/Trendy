package com.example.jmmil.trendy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.astuetz.PagerSlidingTabStrip;


public class MainActivity extends FragmentActivity {

    private EditText searchEditText;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        tabs.setIndicatorColorResource(R.color.slider_bar_color);
        tabs.setTextColorResource(R.color.text_color);

        searchEditText = (EditText) findViewById(R.id.tagEditText);
        searchButton = (ImageButton) findViewById(R.id.searchImageButton);
        searchButton.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener(){

        public void onClick(View v) {
            String query = searchEditText.getText().toString();
            updatePages(query);
        }
    };

    private void updatePages(String query){

    }
}

class MyPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = {"Giphy","Imgur"};

    public MyPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(position == 0){
            fragment = new SimpleGalleryFragment();
        }
        else if(position == 1){
            fragment = new SimpleGalleryFragment();
        }
        else fragment = new SimpleGalleryFragment();

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
