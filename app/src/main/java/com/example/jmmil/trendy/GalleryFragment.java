package com.example.jmmil.trendy;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;

public class GalleryFragment extends Fragment {

    GlideView glideView1;
    GlideView glideView2;
    GlideView glideView3;
    GlideView glideView4;
    GlideView glideView5;
    GlideView glideView6;
    GlideView glideView7;
    GlideView glideView8;
    GlideView glideView9;

    GlideView previewGlideView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        glideView1 = (GlideView) view.findViewById(R.id.glideView1);
        glideView1.setOnClickListener(glideViewListener);
        glideView2 = (GlideView) view.findViewById(R.id.glideView2);
        glideView2.setOnClickListener(glideViewListener);
        glideView3 = (GlideView) view.findViewById(R.id.glideView3);
        glideView3.setOnClickListener(glideViewListener);
        glideView4 = (GlideView) view.findViewById(R.id.glideView4);
        glideView4.setOnClickListener(glideViewListener);
        glideView5 = (GlideView) view.findViewById(R.id.glideView5);
        glideView5.setOnClickListener(glideViewListener);
        glideView6 = (GlideView) view.findViewById(R.id.glideView6);
        glideView6.setOnClickListener(glideViewListener);
        glideView7 = (GlideView) view.findViewById(R.id.glideView7);
        glideView7.setOnClickListener(glideViewListener);
        glideView8 = (GlideView) view.findViewById(R.id.glideView8);
        glideView8.setOnClickListener(glideViewListener);

        glideView9 = (GlideView) view.findViewById(R.id.glideView9);
        glideView9.setOnClickListener(glideViewListener);

        previewGlideView = (GlideView) view.findViewById(R.id.previewGlideView);

        loadGlide();

        return view;
    }

    private void loadGlide() {

        glideView1.setGlideView("https://media0.giphy.com/media/3rgXBDGArplt1PJodi/200w.gif");

        glideView2.setGlideView("https://media2.giphy.com/media/TSeUOygJDayVG/200w.gif");

        glideView3.setGlideView("https://media2.giphy.com/media/dpYkMt1b9BNmw/200w.gif");

        glideView4.setGlideView("https://media0.giphy.com/media/3rgXBDGArplt1PJodi/200w.gif");

        glideView5.setGlideView("https://media2.giphy.com/media/TSeUOygJDayVG/200w.gif");

        glideView6.setGlideView("https://media2.giphy.com/media/dpYkMt1b9BNmw/200w.gif");

        glideView7.setGlideView("https://media0.giphy.com/media/3rgXBDGArplt1PJodi/200w.gif");

        glideView8.setGlideView("https://media2.giphy.com/media/TSeUOygJDayVG/200w.gif");

        glideView9.setGlideView("https://media0.giphy.com/media/m6EMx4ZWHaSUU/200w.gif");

        previewGlideView.setGlideView("https://media2.giphy.com/media/dpYkMt1b9BNmw/200w.gif");

    }

    private OnClickListener glideViewListener = new OnClickListener(){

        public void onClick(View v) {

            GlideView gv = (GlideView) v;

            // change previewGlideView to url that is in gv
            previewGlideView.setGlideView(gv.getURL());
        }
    };
}
