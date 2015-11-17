package com.example.jmmil.trendy;

import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SimpleGalleryFragment2 extends Fragment {

    GlideView glideView1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.simple_fragment_gallery, container, false);

        glideView1 = (GlideView) view.findViewById(R.id.glideView1);
        glideView1.setOnClickListener(glideViewListener);

        loadGlide();

        return view;
    }

    private void loadGlide() {

        glideView1.setGlideView("https://media2.giphy.com/media/TSeUOygJDayVG/200w.gif");

    }

    private OnClickListener glideViewListener = new OnClickListener(){

        public void onClick(View v) {

            GlideView gv = (GlideView) v;

            // change previewGlideView to url that is in gv
            //previewGlideView.setGlideView(gv.getURL());
        }
    };
}
