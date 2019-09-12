package com.sogou.solopiapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    Button upload, download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upload = findViewById(R.id.upload);
        download = findViewById(R.id.download);

        upload.setOnClickListener(this);
        download.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upload:
                NetManager.upload(this);
//                RouterBus.getInstance().build("/LoginActivity").navigation(this);
                break;

            case R.id.download:
                NetManager.download();
//                ARouter.getInstance().build("/test/activity").navigation();
                break;
        }
    }
}
