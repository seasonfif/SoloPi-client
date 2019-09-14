package com.sogou.solopiapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CaseListActivity extends FragmentActivity {

    Button many;
    ListView case_list;
    CaseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);

        many = findViewById(R.id.many);
        case_list = findViewById(R.id.case_list);

        case_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Case aCase = adapter.getItem(i);
                NetManager.download(aCase.rname, aCase.name);

                return true;
            }
        });

        adapter = new CaseAdapter(this);
        case_list.setAdapter(adapter);

        getCaseData();
    }

    private void getCaseData() {
        NetManager.getCases(new NetCallback<List<Case>>() {
            @Override
            public void onSuccess(List<Case> cases) {
                adapter.setData(cases);
                updateCases(cases);
            }

            @Override
            public void onFailed() {
                List<Case> data = new ArrayList<>();
                for (int i=0;i<50; i++){
                    data.add(new Case("net.json", "feed", "SogouBrowser"));
                }
                adapter.setData(data);
            }
        });
    }

    private void updateCases(List<Case> cases) {

        for (Case aCase : cases) {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "seasonfif/"+aCase.name);
            aCase.exists = file.exists();
        }

        adapter.setData(cases);
    }
}
