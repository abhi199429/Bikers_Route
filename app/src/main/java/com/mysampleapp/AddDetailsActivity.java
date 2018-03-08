package com.mysampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddDetailsActivity extends AppCompatActivity {
    private Button Ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        Ok = (Button) findViewById(R.id.Ok);
        Ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDetailsActivity.this,RoutesActivity.class);
                startActivity(intent);

            }
        });
    }
}