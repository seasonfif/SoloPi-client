package com.sogou.solopiapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.sogou.annotation.RouterSchema;

@RouterSchema("/LoginActivity")
public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    Button upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        upload = findViewById(R.id.upload);

        upload.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload:
                break;
        }
    }
}
