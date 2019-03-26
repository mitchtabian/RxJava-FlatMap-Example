package com.codingwithmitch.rxjavaflatmapexample;

import android.os.Bundle;
import android.widget.TextView;


import com.codingwithmitch.rxjavaflatmapexample.models.Post;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewPostActivity extends AppCompatActivity {

    private static final String TAG = "ViewPostActivity";

    private TextView text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        text = findViewById(R.id.text);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("post")){
            Post post = getIntent().getParcelableExtra("post");
            text.setText(post.getTitle());
        }
    }
}


















