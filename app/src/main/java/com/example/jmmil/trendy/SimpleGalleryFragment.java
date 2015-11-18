package com.example.jmmil.trendy;

//import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class SimpleGalleryFragment extends Fragment {

    GlideView previewGlideView;


    /*
    Integer[] imageIDs ={R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif,
            R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif,
            R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif,
            R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif, R.raw.ted_gif};
    */

    String[] imageIDs = {
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2,
            "android.resource://com.example.jmmil.trendy/" + R.raw.ted_gif,
            "android.resource://com.example.jmmil.trendy/" + R.raw.ted_gif,
            "android.resource://com.example.jmmil.trendy/" + R.raw.ted_gif,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file3,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file3,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file3,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file1,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file1,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.file1,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2,
            "android.resource://com.example.jmmil.trendy/" + R.drawable.icon2
    };




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.simple_fragment_gallery, container, false);


        //previewGlideView = (GlideView) view.findViewById(R.id.previewGlideView);
        //previewGlideView.setGlideView("https://media0.giphy.com/media/3rgXBDGArplt1PJodi/200w.gif");

        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(getContext()));


        // TODO
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {

            }
        });


        return view;
    }

    private OnClickListener glideViewListener = new OnClickListener(){

        public void onClick(View v) {

            //TODO
            GlideView gv = (GlideView) v;

            // change previewGlideView to url that is in gv
            //previewGlideView.setGlideView(gv.getURL());
        }
    };


    public class ImageAdapter extends BaseAdapter
    {
        private Context context;

        public ImageAdapter(Context c)
        {
            context = c;
        }

        //---returns the number of images---
        public int getCount() {
            return imageIDs.length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns a populated GlideView view---
        public View getView(int position, View convertView, ViewGroup parent)
        {
            GlideView glideView = new GlideView(context);
            glideView.setGlideView(imageIDs[position]);
            return glideView;

        }
    }
}
