package com.example.jmmil.trendy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SelectedItemActivity extends AppCompatActivity {

    private String url;
    private GlideView glideView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("url");
        }

        glideView = (GlideView) findViewById(R.id.glideView);
        glideView.setGlideView(url);

        textView = (TextView) findViewById(R.id.urlTextView);
        textView.setText(url);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener(){

        public void onClick(View v) {

            Toast.makeText(v.getContext(), url, Toast.LENGTH_LONG).show();
        }
    };
}
