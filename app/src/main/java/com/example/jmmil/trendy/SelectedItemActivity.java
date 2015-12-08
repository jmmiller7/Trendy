package com.example.jmmil.trendy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectedItemActivity extends AppCompatActivity {

    private String url;
    private GlideView glideView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        Bundle extras = getIntent().getExtras();
        if (extras != null) { url = extras.getString("url"); }

        glideView = (GlideView) findViewById(R.id.glideView);
        glideView.setGlideView(url);

        textView = (TextView) findViewById(R.id.urlTextView);
        textView.setText(url);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener(){

        public void onClick(View v) {
            shareTextUrl();
        }
    };

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Share an image from Trendy!");
        share.putExtra(Intent.EXTRA_TEXT, "Image shared from Trendy: " + url);

        startActivity(Intent.createChooser(share, "Share the image!"));
    }
}
