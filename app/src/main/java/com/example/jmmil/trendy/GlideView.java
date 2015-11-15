package com.example.jmmil.trendy;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by jmmil on 11/14/2015.
 */
public class GlideView extends ImageView{

    String url;
    Context context;

    public GlideView(Context context)
    {
        super(context);
        this.context = context;
    }

    public GlideView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
    }

    public GlideView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);

    }

    public String getURL(){
        return url;
    }

    public void setGlideView(String url){
        this.url = url;
        Glide.with(context)
                .load(url)
                .into(this);
    }
}
