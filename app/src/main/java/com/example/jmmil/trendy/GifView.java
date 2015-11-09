package com.example.jmmil.trendy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

/**
 * A majority of the code in this class was originally created by Indragni Soft Solutions.
 * The video can be found at https://www.youtube.com/watch?v=vZonJM5z_Pc
 */
public class GifView extends View {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long movieStart;

    public GifView(Context context){
        super(context);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setFocusable(true);

        gifInputStream = context.getResources().openRawResource(R.raw.ted_gif);
        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();


    }

    protected void onMeasure(int widthMesaureSpec, int heightMeasureSpec){
        setMeasuredDimension(widthMesaureSpec, heightMeasureSpec);
    }

    public int getMovieWidth(){
        return movieWidth;
    }

    public int getMovieHeight(){
        return movieHeight;
    }

    public long getMovieDuration(){
        return movieDuration;
    }

    public void onDraw(Canvas canvas){
        long now = SystemClock.uptimeMillis();

        if(movieStart == 0){
            movieStart = now;
        }

        if(gifMovie != null){
            int dur = gifMovie.duration();
            if(dur == 0)
                dur = 1000;

            int relTime = (int)((now - movieStart) % dur);

            gifMovie.setTime(relTime);

            double scaleX = (double) this.getWidth() / (double) gifMovie.width();
            double scaleY = (double) this.getHeight() / (double) gifMovie.height();
            canvas.scale((float) scaleX, (float) scaleY);

            gifMovie.draw(canvas, 0, 0);
            invalidate();
        }
    }
}
