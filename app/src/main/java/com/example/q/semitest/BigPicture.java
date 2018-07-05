package com.example.q.semitest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class BigPicture extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigpicture);
        mImageView = (ImageView) findViewById(R.id.picture);

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        mImageView.setImageURI(Uri.parse(path));
    }
}
