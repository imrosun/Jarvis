package com.example.googleassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/search?q=idli"));
        startActivity(browserIntent);
//        Uri uri = Uri.parse("https://www.google.com/search?q=idli");
//        Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(gSearchIntent);
    }
}
