package com.example.jmmil.trendy;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class SimpleGalleryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private int NUM_COLUMNS=3;

    String[] imageIDs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.simple_fragment_gallery, container, false);

        imageIDs = populateURLS();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), NUM_COLUMNS));
        mRecyclerView.setAdapter(new GalleryFragmentAdapter());

        return view;
    }


    private String[] populateURLS(){
        String[] imageIDs = {
                "http://media2.giphy.com/media/1XoXJkOGri6Hu/200_d.gif",
                "http://media0.giphy.com/media/6Lt0PMyvjTX8c/200_d.gif",
                "http://media1.giphy.com/media/xTiTnJxChUjF4u958A/200_d.gif",
                "http://media3.giphy.com/media/MJs7EYwHyG8XC/200_d.gif",
                "http://media1.giphy.com/media/14rACYMwBmXDqM/200_d.gif",
                "http://media2.giphy.com/media/3XtigW1mlKBW0/200_d.gif",
                "http://media1.giphy.com/media/xTiTnLRQtRkaRKkKWs/200_d.gif",
                "http://media2.giphy.com/media/3oEduZkX5W1nW0PqOk/200_d.gif",
                "http://media2.giphy.com/media/l41lJa0rg1KO3nZAs/200_d.gif",
                "http://media1.giphy.com/media/xTiTnybAG7cYyzzvQk/200_d.gif",
                "http://media4.giphy.com/media/3oEduSLalG3rotykI8/200_d.gif",
                "http://media3.giphy.com/media/l41lSxrJMaGyxyBpu/200_d.gif",
                "http://media3.giphy.com/media/n8jpGuNug3LIQ/200_d.gif"
        };

        return imageIDs;
    }



    private OnClickListener glideViewListener = new OnClickListener(){

        public void onClick(View v) {

            //TODO
            GlideView gv = (GlideView) v;
            String url = gv.getURL();

            Intent intent = new Intent(SimpleGalleryFragment.this.getContext(), SelectedItemActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    };


    public class GalleryFragmentAdapter extends RecyclerView.Adapter<GlideViewHolder>
    {
        @Override
        public GlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            GlideView gv = new GlideView(getContext());
            gv.setOnClickListener(glideViewListener);
            return new GlideViewHolder(gv);
        }

        @Override
        public void onBindViewHolder(GlideViewHolder holder, int position)
        {
            String uri = imageIDs[position];
            holder.setImageURL(uri);
        }

        @Override
        public int getItemCount()
        {
            return imageIDs.length;
        }
    }

    public class GlideViewHolder extends RecyclerView.ViewHolder
    {
        private GlideView mGlideView;

        public GlideViewHolder(View itemView)
        {
            super(itemView);
            mGlideView = (GlideView) itemView;

        }

        public void setImageURL(String imageURL)
        {
            mGlideView.setGlideView(imageURL);
        }
    }
}
